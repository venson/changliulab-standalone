package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulabstandalone.entity.dto.SectionContentDTO;
import com.venson.changliulabstandalone.entity.dto.SectionPreviewDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.pojo.EduSection;
import com.venson.changliulabstandalone.entity.pojo.EduSectionMarkdown;
import com.venson.changliulabstandalone.mapper.EduSectionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.service.admin.EduSectionMarkdownService;
import com.venson.changliulabstandalone.service.admin.EduSectionService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-06-13
 */
@Service
public class EduSectionServiceImp extends ServiceImpl<EduSectionMapper, EduSection> implements EduSectionService {

    @Autowired
    private EduSectionMarkdownService markdownService;


    @Autowired
    private Comparator<ReviewStatus> reviewComparator;
    @Autowired
    private Comparator<ReviewStatus> viewComparator;

    @Override
    public void updateSectionById(Long sectionId, SectionContentDTO sectionContentDTO) {
        EduSection eduSection = new EduSection();
        EduSectionMarkdown eduSectionMarkdown = new EduSectionMarkdown();
        BeanUtils.copyProperties(sectionContentDTO, eduSection);

        eduSection.setIsModified(true);
        eduSectionMarkdown.setMarkdown(sectionContentDTO.getMarkdown());
        eduSectionMarkdown.setHtmlBrBase64(sectionContentDTO.getHtmlBrBase64());
        eduSectionMarkdown.setId(sectionId);
        baseMapper.updateById(eduSection);
        markdownService.updateById(eduSectionMarkdown);
    }

    @Override
    public SectionContentDTO getSectionById(Long sectionId) {
        EduSection section = baseMapper.selectById(sectionId);
        EduSectionMarkdown sectionMd = markdownService.getById(sectionId);
        SectionContentDTO sectionContentDTO = new SectionContentDTO();
        BeanUtils.copyProperties(section, sectionContentDTO);
        sectionContentDTO.setMarkdown(sectionMd.getMarkdown());
        sectionContentDTO.setHtmlBrBase64(sectionMd.getHtmlBrBase64());
        return sectionContentDTO;
    }

    @Override
    public Long addSection(SectionContentDTO section) {
        EduSection eduSection = new EduSection();
        BeanUtils.copyProperties(section, eduSection);
        EduSectionMarkdown sectionMarkdown = new EduSectionMarkdown();
        baseMapper.insert(eduSection);
        // use the same id to save section markdown.
        sectionMarkdown.setId(eduSection.getId());
        sectionMarkdown.setMarkdown(section.getMarkdown());
        markdownService.save(sectionMarkdown);
        return eduSection.getId();
    }

    @Override
    @Transactional
    public void removeMarkSectionById(Long sectionId) {
        EduSection eduSection = baseMapper.selectById(sectionId);
        Assert.notNull(eduSection, "Section not exists");
        eduSection.setIsRemoveAfterReview(!eduSection.getIsRemoveAfterReview());
        baseMapper.updateById(eduSection);
    }

//    @Override
//    public Long addEmptySection(Long courseId, Long chapterId) {
//        // auto set sort for section ,get the count of sections under the courseId and chapterId
//        LambdaQueryWrapper<EduSection> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(EduSection::getCourseId, courseId).eq(EduSection::getChapterId, chapterId);
//        Long selectCount = baseMapper.selectCount(wrapper);
//        // add new Empty Section.
//        EduSection eduSection = new EduSection();
//        eduSection.setTitle("New Section");
//        eduSection.setCourseId(courseId);
//        eduSection.setChapterId(chapterId);
//        eduSection.setSort(Math.toIntExact(++selectCount));
//        baseMapper.insert(eduSection);
//        // add section markdown with the same id
//        EduSectionMarkdown sectionMarkdown = new EduSectionMarkdown();
//        sectionMarkdown.setId(eduSection.getId());
//        sectionMarkdown.setMarkdown("Please Edit");
//        markdownService.save(sectionMarkdown);
//        return eduSection.getId();
//    }

    @Override
    public SectionPreviewDTO getSectionPreviewBySectionId(Long id) {
        EduSection section = baseMapper.selectById(id);
        EduSectionMarkdown markdown = markdownService.getById(id);
        SectionPreviewDTO preview = new SectionPreviewDTO();
        BeanUtils.copyProperties(section,preview);
        preview.setId(id);
        preview.setHtmlBrBase64(markdown.getHtmlBrBase64());
        return preview;
    }

//    @Override
//    public Set<Long> getAppliedSectionCourseIdSet() {
//        LambdaQueryWrapper<EduSection> wrapper = Wrappers.<EduSection>query()
//                .select("DISTINCT courseId").lambda()
//                .eq(EduSection::getReview, ReviewStatus.APPLIED);
//        List<Long> ids = this.listObjs(wrapper, o -> Long.valueOf(o.toString()));
//        return new HashSet<>(ids);
//    }

    @Override
    public Map<Long, ReviewStatus> getSectionReviewStatusMap(boolean review) {

        Comparator<ReviewStatus> comparator = review? reviewComparator: viewComparator;
        LambdaQueryWrapper<EduSection> wrapper = Wrappers.<EduSection>query()
                .select("DISTINCT course_id, review").lambda()
                .eq(EduSection::getReview, ReviewStatus.APPLIED);
        List<EduSection> sections = baseMapper.selectList(wrapper);
        HashMap<Long, ReviewStatus> courseIdSectionReviewMap = new HashMap<>();
        sections.forEach(section -> {
            if (!courseIdSectionReviewMap.containsKey(section.getCourseId())) {
                courseIdSectionReviewMap.put(section.getCourseId(), section.getReview());
            } else {
                ReviewStatus current = courseIdSectionReviewMap.get(section.getCourseId());
                int compare =comparator.compare(current, section.getReview());
                if(compare < 0){
                    courseIdSectionReviewMap.put(section.getCourseId(), section.getReview());
                }
            }
        });

        return courseIdSectionReviewMap;
    }


}
