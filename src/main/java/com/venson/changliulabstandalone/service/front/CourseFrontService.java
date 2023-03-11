package com.venson.changliulabstandalone.service.front;

import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.entity.pojo.EduCoursePublished;
import com.venson.changliulabstandalone.entity.front.dto.CourseFrontBriefDTO;
import com.venson.changliulabstandalone.entity.front.dto.SectionFrontDTO;
import com.venson.changliulabstandalone.entity.front.vo.CourseFrontInfoDTO;
import com.venson.changliulabstandalone.entity.front.vo.CourseFrontTreeNodeVo;

import java.util.List;

public interface CourseFrontService {
    CourseFrontInfoDTO getFrontCourseInfo(Long id);

    PageResponse<EduCoursePublished> getPageCourseByMemberId(Long id, Integer page, Integer limit);

    SectionFrontDTO getSectionBySectionId(Long id);

//    ChapterFrontDTO getChapterByChapterId(Long id);

    List<CourseFrontBriefDTO> getFrontIndexCourse();


    List<CourseFrontTreeNodeVo> getCourseFrontTreeByCourseId(Long id);
}
