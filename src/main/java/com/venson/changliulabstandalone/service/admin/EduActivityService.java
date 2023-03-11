package com.venson.changliulabstandalone.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.entity.pojo.EduActivity;
import com.venson.changliulabstandalone.entity.dto.ActivityAdminDTO;
import com.venson.changliulabstandalone.entity.dto.ActivityPreviewDTO;

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


    ActivityAdminDTO getActivityById(Long id);

    ActivityPreviewDTO getPreviewByActivityId(long id);

    Long addActivity(ActivityAdminDTO activityAdminDTO);
}
