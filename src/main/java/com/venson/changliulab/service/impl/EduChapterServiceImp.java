package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.entity.dto.ChapterContentDTO;
import com.venson.changliulab.entity.dto.ReviewTargetDTO;
import com.venson.changliulab.entity.dto.ShowChapterDTO;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.front.dto.ChapterFrontDTO;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.*;
import com.venson.changliulab.entity.vo.CourseTreeNodeVo;
import com.venson.changliulab.entity.vo.admin.CommonMetaVo;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import com.venson.changliulab.exception.CustomizedException;
import com.venson.changliulab.mapper.EduChapterMapper;
import com.venson.changliulab.service.admin.*;
import com.venson.changliulab.utils.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.function.Predicate;
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
@RequiredArgsConstructor
public class EduChapterServiceImp extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService, ReviewableService {


    private final EduChapterDescriptionService descService;
    private final EduChapterPublishedService chapterPublishedService;
    private final EduChapterPublishedDescService chapterPublishedDescService;
    private final EduSectionService sectionService;
    private final Comparator<ReviewStatus> reviewComparator;
    private final Comparator<ReviewStatus> viewComparator;
//    private final EduCourseService courseService;


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
    public ChapterContentDTO getChapterDTOById(Long chapterId, CommonMetaVo vo) {
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

    @Override
    public boolean existsByIds(List<Long> ids) {
        if(ids.isEmpty()) return false;
        LambdaQueryWrapper<EduChapter> wrapper = Wrappers.<EduChapter>query().lambda()
                .in(EduChapter::getId, ids);
        return count(wrapper) == ids.size();
//        return false;
    }

    @Override
    @Transactional
    public void updateReviewStatusByIds(List<Long> ids, ReviewStatus reviewStatus) {
        if(ids.isEmpty()) return;
        LambdaUpdateWrapper<EduChapter> wrapper = Wrappers.<EduChapter>update().lambda()
                .set(EduChapter::getReview, reviewStatus)
                .in(EduChapter::getId, ids);
        baseMapper.update(null,wrapper);

    }

    @Override
    public Long getCourseId(ReviewSMContext context) {
        if(context.ids().isEmpty()) return null;
        EduChapter chapter = baseMapper.selectById(context.ids().get(0));
        if(chapter != null) return chapter.getCourseId();
        return null;
    }

    @Override
    @Transactional
    public void publishReviewedChapters(List<Long> ids) {
        if(ids.isEmpty()) return;

        LambdaQueryWrapper<EduChapter> chapterWrapper = Wrappers.lambdaQuery(EduChapter.class)
                .in(EduChapter::getId,ids)
                .eq(EduChapter::getReview, ReviewStatus.APPLIED);
        List<EduChapter> appliedChapters = baseMapper.selectList(chapterWrapper);
        if(appliedChapters ==null || appliedChapters.isEmpty()) return ;
        try {
            List<Long> removedChapterIds = appliedChapters.stream().filter(EduChapter::getIsRemoveAfterReview).map(EduChapter::getId).collect(Collectors.toList());
            if(!removedChapterIds.isEmpty()){
                baseMapper.deleteBatchIds(removedChapterIds);
                descService.removeByIds(removedChapterIds);
                chapterPublishedService.removeByIds(removedChapterIds);
                chapterPublishedDescService.removeByIds(removedChapterIds);
            }
        } catch (NullPointerException ignore){}

        try{
            List<Long> modifiedChapterIds = appliedChapters.stream().filter(Predicate.not(EduChapter::getIsRemoveAfterReview)).map(EduChapter::getId).collect(Collectors.toList());
            List<EduChapterPublished> modifiedChapters = appliedChapters.stream().filter(Predicate.not(EduChapter::getIsRemoveAfterReview))
                    .map(chapter -> EduChapterPublished.builder()
                            .courseId(chapter.getCourseId())
                            .id(chapter.getId())
                            .title(chapter.getTitle())
                            .sort(chapter.getSort())
                            .build()

                    ).collect(Collectors.toList());
            chapterPublishedService.saveOrUpdateBatch(modifiedChapters);
            List<EduChapterDescription> modifiedChapterDescs = descService.listByIds(modifiedChapterIds);
            List<EduChapterPublishedDesc> modifiedChapterPublishedDescs = modifiedChapterDescs.stream()
                    .map(desc -> EduChapterPublishedDesc.builder()
                            .id(desc.getId())
                            .description(desc.getDescription())
                            .build()
                    ).collect(Collectors.toList());

            chapterPublishedDescService.saveOrUpdateBatch(modifiedChapterPublishedDescs);


        }catch (NullPointerException ignore){}
    }

    @Override
    public ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id) {
        EduChapter chapterApplied = baseMapper.selectById(id);
        Assert.notNull(chapterApplied,"Chapter not found");
        EduChapterPublished  chapterPublished= chapterPublishedService.getById(id);
        EduChapterDescription chapterDescApplied = descService.getById(id);
        EduChapterPublishedDesc chapterDescPublished= chapterPublishedDescService.getById(id);

        ShowChapterDTO applied = ShowChapterDTO.builder()
                .title(chapterApplied.getTitle())
                .description(chapterDescApplied.getDescription())
                .build();
        ShowChapterDTO reviewed = null;

        if(chapterPublished!= null){
            reviewed = ShowChapterDTO.builder()
                    .title(chapterPublished.getTitle())
                    .description(chapterDescPublished.getDescription())
                    .build();
        }
        return ReviewTargetDTO.<ReviewAble>builder()
                .id(id)
                .status(chapterApplied.getReview())
                .applied(applied)
                .reviewed(reviewed)
                .build();
    }

//    @Override
//    public Collection<? extends ReviewBasicDTO> getInfoByReviews(List<EduReview> values) {
//        List<Long> chapterIdsList = values.stream().map(EduReview::getRefId).toList();
//        LambdaQueryWrapper<EduChapter> chapterWrapper = Wrappers.lambdaQuery();
//        chapterWrapper.in(EduChapter::getId,chapterIdsList).select(EduChapter::getId,EduChapter::getTitle, EduChapter::getCourseId);
//        // get all chapters
//        List<EduChapter> chapterList = baseMapper.selectList(chapterWrapper);
//
//
//        courseService.listByIds(chapterIdsList);
//
//        return null;
//    }


}
