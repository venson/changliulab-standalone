package com.venson.changliulabstandalone.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewType;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.vo.admin.ListQueryParams;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.entity.pojo.EduCourse;
import com.venson.changliulabstandalone.entity.dto.CourseInfoDTO;
import com.venson.changliulabstandalone.entity.dto.CoursePageDTO;
import com.venson.changliulabstandalone.entity.dto.CoursePreviewVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-05-11
 */
public interface EduCourseService extends IService<EduCourse> {

    Long addCourse(CourseInfoDTO courseInfoDTO);

    CourseInfoDTO getCourseById(Long id);

    void updateCourse(CourseInfoDTO infoVo);

    void setRemoveMarkByCourseById(Long courseId);


    PageResponse<EduCourse> getPageCoursePublishVo(Integer pageNum, Integer limit, String condition);

    CoursePreviewVo getCoursePreviewById(Long courseId);


    PageResponse<CoursePageDTO> getCoursePageReview(Integer current, Integer size);

    PageResponse<CoursePageDTO> getCoursePage(Integer current, Integer size, String condition);

    PageResponse<EduCourse> getPageCoursePublishVo(ListQueryParams vo);

    Collection<? extends ReviewBasicDTO> getInfoByReviews(Map<ReviewType, List<EduReview>> courseMap);
}
