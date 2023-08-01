package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulab.entity.dto.*;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduSection;
import com.venson.changliulab.entity.pojo.EduSectionMarkdown;
import com.venson.changliulab.entity.pojo.EduSectionPublished;
import com.venson.changliulab.entity.pojo.EduSectionPublishedMd;
import com.venson.changliulab.mapper.EduSectionMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.service.admin.*;
import com.venson.changliulab.utils.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-06-13
 */
@Service
@RequiredArgsConstructor
public class EduSectionServiceImp extends ServiceImpl<EduSectionMapper, EduSection> implements EduSectionService, ReviewableService {

    private final EduSectionMarkdownService markdownService;
    private final EduSectionPublishedService  sectionPublishedService;
    private final EduSectionPublishedMdService sectionPublishedMdService;

    private final Comparator<ReviewStatus> reviewComparator;
    private final Comparator<ReviewStatus> viewComparator;
//    private final EduContentService contentService;



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

    @Override
    public Long getCourseIdById(Long refId) {
        EduSection eduSection = baseMapper.selectById(refId);
        Assert.notNull(eduSection, "Section not found");
        return eduSection.getCourseId();
    }

    @Override
    public void updateReviewStatusByIds(List<Long> ids, ReviewStatus reviewStatus) {
        LambdaUpdateWrapper<EduSection> wrapper = Wrappers.<EduSection>update().lambda()
                .set(EduSection::getReview, reviewStatus)
                .in(EduSection::getId, ids);
        baseMapper.update(null,wrapper);

    }

    @Override
    public boolean exitsByIds(List<Long> ids, ReviewStatus... reviewStatus) {
        if (ids.isEmpty() || reviewStatus.length ==0) return false;
        LambdaQueryWrapper<EduSection> wrapper = Wrappers.<EduSection>query().lambda()
                .in(EduSection::getReview, List.of(reviewStatus))
                .in(EduSection::getId, ids);
        int count = baseMapper.selectCount(wrapper).intValue();
        return count == ids.size();
    }

    @Override
    public void publishReviewedSection(List<Long> ids) {
        LambdaQueryWrapper<EduSection> wrapper = Wrappers.<EduSection>query().lambda()
                .in(EduSection::getId, ids)
                .eq(EduSection::getReview, ReviewStatus.APPLIED);

        List<EduSection> sections = baseMapper.selectList(wrapper);
        if (sections.isEmpty()) return;
        List<EduSectionPublished> sectionPublisheds = sections.stream().map(section -> {
            EduSectionPublished sectionPublished = new EduSectionPublished();
            BeanUtils.copyProperties(section, sectionPublished);
            return sectionPublished;
        }).collect(Collectors.toList());
        List<Long> idList = sections.stream().map(EduSection::getId).collect(Collectors.toList());
        sectionPublishedService.saveOrUpdateBatch(sectionPublisheds);
        LambdaQueryWrapper<EduSectionMarkdown> mdWrapper = Wrappers.<EduSectionMarkdown>query().lambda()
                .in(EduSectionMarkdown::getId,idList);
        List<EduSectionMarkdown> markdowns = markdownService.list(mdWrapper);
        List<EduSectionPublishedMd> sectionPublishedMds = markdowns.stream().map(markdown -> {
            EduSectionPublishedMd sectionPublishedMd = new EduSectionPublishedMd();
            BeanUtils.copyProperties(markdown, sectionPublishedMd);
            return sectionPublishedMd;
        }).collect(Collectors.toList());
        sectionPublishedMdService.saveOrUpdateBatch(sectionPublishedMds);

    }


    @Override
    public ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id) {
        EduSection sectionApplied= baseMapper.selectById(id);
        Assert.notNull(sectionApplied,"Section not found");
        EduSectionMarkdown markdownApplied = markdownService.getById(id);
        EduSectionPublished sectionReviewed = sectionPublishedService.getById(id);
        EduSectionPublishedMd sectionMdReviewed = sectionPublishedMdService.getById(id);

        ShowSectionDTO applied = ShowSectionDTO.builder()
                .title(sectionApplied.getTitle())
                .html(markdownApplied.getHtmlBrBase64())
                .review(sectionApplied.getReview())
                .videoLink(sectionApplied.getVideoLink())
                .isRemoveAfterReview(sectionApplied.getIsRemoveAfterReview())
                .build();

        ShowSectionDTO reviewed = null;
        if(sectionReviewed!=null){
            reviewed = ShowSectionDTO.builder()
                    .title(sectionReviewed.getTitle())
                    .html(sectionMdReviewed.getHtmlBrBase64())
                    .videoLink(sectionReviewed.getVideoLink())
                    .build();
        }
        return  ReviewTargetDTO.<ReviewAble>builder()
                .id(id).status(sectionApplied.getReview())
                .applied(applied)
                .reviewed(reviewed)
                .build();

    }
}
