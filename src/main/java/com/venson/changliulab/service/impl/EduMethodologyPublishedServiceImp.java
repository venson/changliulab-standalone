package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.entity.dto.ShowMethodologyDTO;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduMethodologyPublished;
import com.venson.changliulab.mapper.EduMethodologyPublishedMapper;
import com.venson.changliulab.service.admin.EduMethodologyPublishedService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
@Service
public class EduMethodologyPublishedServiceImp extends ServiceImpl<EduMethodologyPublishedMapper, EduMethodologyPublished> implements EduMethodologyPublishedService {
    @Override
    public ReviewAble getReviewById(Long refId) {

        EduMethodologyPublished eduMethodology = baseMapper.selectById(refId);
        if(eduMethodology == null) return null;
        return ShowMethodologyDTO.builder().isPublic(eduMethodology.getIsPublic())
                .html(eduMethodology.getHtmlBrBase64())
                .title(eduMethodology.getTitle()).build();
    }


}
