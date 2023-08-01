package com.venson.changliulab.service.front;

import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.entity.pojo.EduCoursePublished;
import com.venson.changliulab.entity.front.dto.CourseFrontBriefDTO;
import com.venson.changliulab.entity.front.dto.SectionFrontDTO;
import com.venson.changliulab.entity.front.vo.CourseFrontInfoDTO;
import com.venson.changliulab.entity.front.vo.CourseFrontTreeNodeVo;

import java.util.List;

public interface CourseFrontService {
    CourseFrontInfoDTO getFrontCourseInfo(Long id);

    PageResponse<EduCoursePublished> getPageCourseByMemberId(Long id, Integer page, Integer limit);

    SectionFrontDTO getSectionBySectionId(Long id);

//    ChapterFrontDTO getChapterByChapterId(Long id);

    List<CourseFrontBriefDTO> getFrontIndexCourse();


    List<CourseFrontTreeNodeVo> getCourseFrontTreeByCourseId(Long id);
}
