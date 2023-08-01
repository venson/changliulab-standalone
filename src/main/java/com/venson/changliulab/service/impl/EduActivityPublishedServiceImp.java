package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.entity.dto.ShowActivityDTO;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduActivityPublished;
import com.venson.changliulab.entity.front.dto.ActivityFrontBriefDTO;
import com.venson.changliulab.entity.pojo.EduActivityPublishedMd;
import com.venson.changliulab.mapper.EduActivityPublishedMapper;
import com.venson.changliulab.service.admin.EduActivityPublishedMdService;
import com.venson.changliulab.service.admin.EduActivityPublishedService;
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
        if(eduActivity==null) return null;
        EduActivityPublishedMd markdown = publishedMdService.getById(refId);
        ShowActivityDTO showActivityDTO = new ShowActivityDTO();
        showActivityDTO.setDate(eduActivity.getActivityDate());
        showActivityDTO.setHtml(markdown.getHtmlBrBase64());
        showActivityDTO.setTitle(eduActivity.getTitle());
        return showActivityDTO;
    }
}
