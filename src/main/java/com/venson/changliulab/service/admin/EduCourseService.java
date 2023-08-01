package com.venson.changliulab.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.dto.*;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.enums.ReviewType;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.entity.vo.admin.ListQueryParams;
import com.venson.changliulab.entity.vo.admin.ReviewMetaVo;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.entity.pojo.EduCourse;

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


    ReviewAble getReviewByIdType(Long refId, ReviewType type, ReviewMetaVo metaVo);

    ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id, ReviewType type);

    boolean exitsByIds(List<Long> ids, ReviewStatus... statuses);

    void updateReviewStatusByCourseIds(List<Long> ids, ReviewStatus reviewStatus);

    void publishReviewedCourse(List<Long> ids);
}
