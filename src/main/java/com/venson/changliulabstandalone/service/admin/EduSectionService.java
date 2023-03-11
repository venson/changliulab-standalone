package com.venson.changliulabstandalone.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.pojo.EduSection;
import com.venson.changliulabstandalone.entity.dto.SectionContentDTO;
import com.venson.changliulabstandalone.entity.dto.SectionPreviewDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;

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
}
