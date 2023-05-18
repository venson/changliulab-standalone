package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.dto.MethodologyDTO;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.dto.ShowMethodologyDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import com.venson.changliulabstandalone.entity.pojo.EduMethodology;
import com.venson.changliulabstandalone.entity.pojo.EduMethodologyPublished;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.vo.admin.AdminMethodologyVo;
import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.mapper.EduMethodologyMapper;
import com.venson.changliulabstandalone.mapper.EduMethodologyPublishedMapper;
import com.venson.changliulabstandalone.service.admin.EduMethodologyPublishedService;
import com.venson.changliulabstandalone.service.admin.EduMethodologyService;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.PageUtil;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
        return ShowMethodologyDTO.builder().isPublic(eduMethodology.getIsPublic())
                .html(eduMethodology.getHtmlBrBase64())
                .title(eduMethodology.getTitle()).build();
    }

//    @Override
//    public List<EduMethodology> getMethodologyReviewList() {
//        LambdaQueryWrapper<EduMethodology> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(EduMethodology::getReview, ReviewStatus.APPLIED);
//        return baseMapper.selectList(wrapper);
//    }

}
