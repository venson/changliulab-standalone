package com.venson.changliulab.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduSection;
import com.venson.changliulab.entity.dto.SectionContentDTO;
import com.venson.changliulab.entity.dto.SectionPreviewDTO;
import com.venson.changliulab.entity.enums.ReviewStatus;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2022-06-13
 */
public interface EduSectionService extends IService<EduSection> {


    void updateSectionById(Long sectionId, SectionContentDTO sectionContentDTO);

    SectionContentDTO getSectionById(Long sectionId);

    Long addSection(SectionContentDTO section);

    void removeMarkSectionById(Long sectionId);

//    Long addEmptySection(Long courseId, Long chapterId);

    SectionPreviewDTO getSectionPreviewBySectionId(Long id);

//    Set<Long> getAppliedSectionCourseIdSet();

    Map<Long, ReviewStatus> getSectionReviewStatusMap(boolean review);

    Long getCourseIdById(Long refId);

    void updateReviewStatusByIds(List<Long> ids, ReviewStatus reviewStatus);

    boolean exitsByIds(List<Long> ids, ReviewStatus... reviewStatus);

    void publishReviewedSection(List<Long> ids);
}
