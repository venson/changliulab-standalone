package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.entity.dto.ChapterContentDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.pojo.EduChapter;
import com.venson.changliulabstandalone.entity.pojo.EduChapterDescription;
import com.venson.changliulabstandalone.entity.pojo.EduSection;
import com.venson.changliulabstandalone.entity.vo.CourseTreeNodeVo;
import com.venson.changliulabstandalone.entity.front.dto.ChapterFrontDTO;
import com.venson.changliulabstandalone.exception.CustomizedException;
import com.venson.changliulabstandalone.mapper.EduChapterMapper;
import com.venson.changliulabstandalone.service.admin.EduChapterDescriptionService;
import com.venson.changliulabstandalone.service.admin.EduChapterService;
import com.venson.changliulabstandalone.service.admin.EduSectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-05-11
 */
@Service
@Slf4j
public class EduChapterServiceImp extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {


    @Autowired
    private EduChapterDescriptionService descService;

    @Autowired
    private EduSectionService sectionService;

    @Autowired
    private Comparator<ReviewStatus> reviewComparator;
    @Autowired
    private Comparator<ReviewStatus> viewComparator;

    public EduChapterServiceImp(EduChapterDescriptionService desService) {
        this.descService = desService;
    }


    @Override
    public List<CourseTreeNodeVo> getCourseTreeByCourseId(Long courseId) {

        //get chapter info as parent node
        LambdaQueryWrapper<EduChapter> wrapperChapter = new LambdaQueryWrapper<>();
        wrapperChapter.eq(EduChapter::getCourseId, courseId);
        wrapperChapter.orderBy(true, true, Arrays.asList(EduChapter::getSort, EduChapter::getId));
        List<EduChapter> chapterList = baseMapper.selectList(wrapperChapter);

        List<CourseTreeNodeVo> chapterNodeList = chapterList.stream()
                .map(o -> new CourseTreeNodeVo(o.getId(),
                        o.getTitle(), o.getReview(),
                        o.getIsPublished(), o.getIsModified(), o.getIsRemoveAfterReview()))
                .collect(Collectors.toList());


        //get section info as children node
        LambdaQueryWrapper<EduSection> wrapperSection = new LambdaQueryWrapper<>();
        wrapperSection.eq(EduSection::getCourseId, courseId).
                orderBy(true, true,Arrays.asList( EduSection::getChapterId, EduSection::getSort));
        List<EduSection> sectionList = sectionService.list(wrapperSection);

        // group section  by chapter node and
        Map<Long, List<CourseTreeNodeVo>> sectionGroupMap = sectionList.parallelStream().
                collect(Collectors.groupingBy(EduSection::getChapterId,
                        Collectors.mapping(o -> new CourseTreeNodeVo(o.getId(),
                                        o.getTitle(), o.getReview(), o.getIsPublished(), o.getIsModified(), o.getIsRemoveAfterReview()),
                                Collectors.toList())));

        chapterNodeList.parallelStream().forEach(o -> o.setChildren(sectionGroupMap.get(o.getId())));
        return chapterNodeList;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteChapterSectionByCourseId(Long courseId) {
        LambdaUpdateWrapper<EduChapter> chapterWrapper = new LambdaUpdateWrapper<>();
        LambdaUpdateWrapper<EduSection> sectionWrapper = new LambdaUpdateWrapper<>();
        chapterWrapper.eq(EduChapter::getCourseId, courseId)
                .set(EduChapter::getIsRemoveAfterReview, true);
        sectionWrapper.eq(EduSection::getCourseId, courseId)
                .set(EduSection::getIsRemoveAfterReview, true);
        baseMapper.update(null, chapterWrapper);
        sectionService.update(sectionWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeMarkChapterById(Long chapterId) {
        EduChapter eduChapter = baseMapper.selectById(chapterId);
        Assert.notNull(eduChapter, "Chapter not exists");
        LambdaUpdateWrapper<EduSection> sectionWrapper = Wrappers.lambdaUpdate(EduSection.class)
                .eq(EduSection::getChapterId, chapterId)
                .set(EduSection::getIsRemoveAfterReview, !eduChapter.getIsRemoveAfterReview());

        eduChapter.setIsRemoveAfterReview(!eduChapter.getIsRemoveAfterReview());
        baseMapper.updateById(eduChapter);
        sectionService.update(sectionWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateChapterById(Long chapterId, ChapterFrontDTO chapter) {
        EduChapter eduChapter = baseMapper.selectById(chapterId);
        eduChapter.setIsModified(true);
        EduChapterDescription desc = descService.getById(chapterId);
        eduChapter.setTitle(chapter.getTitle());
        desc.setDescription(chapter.getDescription());
        baseMapper.updateById(eduChapter);
        descService.updateById(desc);
    }

    @Override
    public ChapterContentDTO getChapterDTOById(Long chapterId) {
        EduChapter chapter = baseMapper.selectById(chapterId);
        EduChapterDescription desc = descService.getById(chapterId);
        ChapterContentDTO chapterContentDTO = new ChapterContentDTO();
        BeanUtils.copyProperties(chapter, chapterContentDTO);
        chapterContentDTO.setDescription(desc.getDescription());
        return chapterContentDTO;
    }

    /**
     * add chapter
     *
     * @return chapterId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addChapter(ChapterFrontDTO chapterFrontDTO) {
        if (!ObjectUtils.isEmpty(chapterFrontDTO.getId())) {
            throw new CustomizedException(40000, "chapter already exist");
        }
        EduChapter chapter = new EduChapter();
        EduChapterDescription chapterMarkdown = new EduChapterDescription();
        BeanUtils.copyProperties(chapterFrontDTO, chapter);
        baseMapper.insert(chapter);
        chapterMarkdown.setId(chapter.getId());
        chapterMarkdown.setDescription(chapterFrontDTO.getDescription());
        descService.save(chapterMarkdown);
        return chapter.getId();
    }

    @Override
    public Map<Long, ReviewStatus> getChapterReviewStatusMap(boolean review) {
        Comparator<ReviewStatus> comparator;
        comparator = review? reviewComparator: viewComparator;
        LambdaQueryWrapper<EduChapter> wrapper = Wrappers.<EduChapter>query()
                .select("DISTINCT course_id, review").lambda()
                .eq(EduChapter::getReview, ReviewStatus.APPLIED);
        List<EduChapter> chapters = baseMapper.selectList(wrapper);
        HashMap<Long, ReviewStatus> courseIdChapterReviewMap = new HashMap<>();
        chapters.forEach(chapter -> {
            if (!courseIdChapterReviewMap.containsKey(chapter.getCourseId())) {
                courseIdChapterReviewMap.put(chapter.getCourseId(), chapter.getReview());
            } else {
                ReviewStatus current = courseIdChapterReviewMap.get(chapter.getCourseId());
                int compare =comparator.compare(current, chapter.getReview());
                if(compare < 0){
                    courseIdChapterReviewMap.put(chapter.getCourseId(), chapter.getReview());
                }
            }
        });

        return courseIdChapterReviewMap;
    }


}
