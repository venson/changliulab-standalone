package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.entity.dto.ActivityShowDTO;
import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import com.venson.changliulabstandalone.entity.pojo.EduActivityPublished;
import com.venson.changliulabstandalone.entity.front.dto.ActivityFrontBriefDTO;
import com.venson.changliulabstandalone.entity.pojo.EduActivityPublishedMd;
import com.venson.changliulabstandalone.mapper.EduActivityPublishedMapper;
import com.venson.changliulabstandalone.service.admin.EduActivityPublishedMdService;
import com.venson.changliulabstandalone.service.admin.EduActivityPublishedService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-08-09
 */
@Service
@RequiredArgsConstructor
public class EduActivityPublishedServiceImp extends ServiceImpl<EduActivityPublishedMapper, EduActivityPublished> implements EduActivityPublishedService {
    private final EduActivityPublishedMdService publishedMdService;


    @Override
    public List<ActivityFrontBriefDTO> getFrontIndexActivity() {
        return baseMapper.getFrontIndexActivity();
    }

    @Override
    public ReviewAble getReviewById(Long refId) {
        EduActivityPublished eduActivity = baseMapper.selectById(refId);
        EduActivityPublishedMd markdown = publishedMdService.getById(refId);
        ActivityShowDTO activityShowDTO = new ActivityShowDTO();
        activityShowDTO.setDate(eduActivity.getActivityDate());
        activityShowDTO.setHtml(markdown.getHtmlBrBase64());
        activityShowDTO.setTitle(eduActivity.getTitle());
        return activityShowDTO;
    }
}
