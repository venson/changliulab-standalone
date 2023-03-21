package com.venson.changliulabstandalone.service.front.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.PageUtil;
import com.venson.changliulabstandalone.entity.pojo.EduActivityPublished;
import com.venson.changliulabstandalone.entity.pojo.EduActivityPublishedMd;
import com.venson.changliulabstandalone.entity.front.dto.ActivityFrontBriefDTO;
import com.venson.changliulabstandalone.entity.front.dto.ActivityFrontDTO;
import com.venson.changliulabstandalone.service.admin.EduActivityPublishedMdService;
import com.venson.changliulabstandalone.service.admin.EduActivityPublishedService;
import com.venson.changliulabstandalone.service.front.ActivityFrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityFrontServiceImpl implements ActivityFrontService {
    @Autowired
    private EduActivityPublishedService activityPublishedService;
    @Autowired
    private EduActivityPublishedMdService activityPublishedMdService;
    @Override
    @Cacheable(value = FrontCacheConst.ACTIVITY_PAGE_NAME,key = "#page")
    public PageResponse<EduActivityPublished> getPageActivity(Integer page, Integer limit) {
        LambdaQueryWrapper<EduActivityPublished> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduActivityPublished::getEnable, true);
        wrapper.select(EduActivityPublished::getId,EduActivityPublished::getTitle,
                EduActivityPublished::getAuthorMemberName,EduActivityPublished::getActivityDate);
        Page<EduActivityPublished> pageActivity = new Page<>(page, limit);
        activityPublishedService.page(pageActivity,wrapper);
        return PageUtil.toBean(pageActivity);
    }

    @Override
    @Cacheable(value = FrontCacheConst.ACTIVITY_NAME,key = "#id")
    public ActivityFrontDTO getActivityById(Long id) {
        EduActivityPublished eduActivity = activityPublishedService.getById(id);
        EduActivityPublishedMd eduActivityMd = activityPublishedMdService.getById(id);
        ActivityFrontDTO activity = new ActivityFrontDTO();
        activity.setActivityDate(eduActivity.getActivityDate());
        activity.setId(id);
        activity.setTitle(eduActivity.getTitle());
        activity.setAuthorMemberName(activity.getAuthorMemberName());
        activity.setHtmlBrBase64(eduActivityMd.getHtmlBrBase64());
        return activity;
    }

    @Override
    @Cacheable(value = FrontCacheConst.HOME_NAME,key = FrontCacheConst.HOME_ACTIVITY_KEY)
    public List<ActivityFrontBriefDTO> getFrontIndexActivity() {
        return activityPublishedService.getFrontIndexActivity();
    }
}
