package com.venson.changliulab.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.dto.ReviewBasicDTO;
import com.venson.changliulab.entity.dto.ReviewTargetDTO;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.entity.vo.admin.CommonMetaVo;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.entity.pojo.EduActivity;
import com.venson.changliulab.entity.dto.ActivityAdminDTO;
import com.venson.changliulab.entity.dto.ActivityPreviewDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
public interface EduActivityService extends IService<EduActivity> {

    PageResponse<EduActivity> getPageReviewList(Integer page, Integer limit);


    void switchEnableByActivityId(Long id);

    PageResponse<EduActivity> getPageActivityList(Integer page, Integer limit,String title,String begin, String end);


    void updateActivity(Long id, ActivityAdminDTO infoVo);

    void deleteActivity(Long id);


    ActivityAdminDTO getActivityById(Long id, CommonMetaVo vo);

    ActivityPreviewDTO getPreviewByActivityId(long id);

    Long addActivity(ActivityAdminDTO activityAdminDTO);

    PageResponse<EduActivity> getPageActivityList(PageQueryVo vo);

    Map<Long, String> getIdTitleMap(List<Long> refIds);

    List<ReviewBasicDTO> getInfoByReviews(List<EduReview> orDefault);

    ReviewAble getReviewById(Long refId);

    ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id);

    boolean existsByIds(List<Long> ids);

    void updateReviewsByIds(List<Long> ids, ReviewStatus reviewStatus);

    void publishActivities(ReviewSMContext ctx);
}
