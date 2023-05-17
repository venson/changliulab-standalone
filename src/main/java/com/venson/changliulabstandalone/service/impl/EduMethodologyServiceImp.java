package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.pojo.EduResearch;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.vo.admin.AdminMethodologyVo;
import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.PageUtil;
import com.venson.changliulabstandalone.entity.pojo.EduMethodology;
import com.venson.changliulabstandalone.entity.dto.MethodologyDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.mapper.EduMethodologyMapper;
import com.venson.changliulabstandalone.service.admin.EduMethodologyService;
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
public class EduMethodologyServiceImp extends ServiceImpl<EduMethodologyMapper, EduMethodology> implements EduMethodologyService {

//    @Override
//    public List<EduMethodology> getMethodologyReviewList() {
//        LambdaQueryWrapper<EduMethodology> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(EduMethodology::getReview, ReviewStatus.APPLIED);
//        return baseMapper.selectList(wrapper);
//    }

    @Override
    public PageResponse<EduMethodology> getMethodologyPage(Integer page, Integer limit) {
        Page<EduMethodology> methodologyPage = new Page<>(page, limit);
        LambdaQueryWrapper<EduMethodology> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EduMethodology::getId, EduMethodology::getTitle,
                EduMethodology::getEnable,EduMethodology::getIsPublic,EduMethodology::getReview,
                EduMethodology::getIsPublished, EduMethodology::getIsModified);
        baseMapper.selectPage(methodologyPage, wrapper);
        return PageUtil.toBean(methodologyPage);
    }

    @Override
    @Cacheable(value = FrontCacheConst.METHODOLOGY_PAGE_NAME, key = "#page +':'+ #isMember")
    public PageResponse<EduMethodology> getMethodologyFrontPage(Integer page, Integer limit,boolean isMember) {
        Page<EduMethodology> methodologyPage = new Page<>(page, limit);
        LambdaQueryWrapper<EduMethodology> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EduMethodology::getId, EduMethodology::getTitle);
        wrapper.eq(! isMember,EduMethodology::getIsPublic, true).eq(EduMethodology::getIsPublished,true);

        baseMapper.selectPage(methodologyPage, wrapper);
        return PageUtil.toBean(methodologyPage);
    }

    @Override
    public MethodologyDTO getMethodologyViewById(Long id) {

        EduMethodology eduMethodology = baseMapper.selectById(id);
        MethodologyDTO view = new MethodologyDTO();
        view.setHtmlBrBase64(eduMethodology.getHtmlBrBase64());
        view.setPublishedHtmlBrBase64(eduMethodology.getPublishedHtmlBrBase64());
        view.setTitle(eduMethodology.getTitle());
        view.setLanguage(eduMethodology.getLanguage());
        view.setReview(eduMethodology.getReview());
        view.setIsPublished(eduMethodology.getIsPublished());
        view.setIsModified(eduMethodology.getIsModified());
        return view;
    }

    @Override
    public Long addMethodology(AdminMethodologyVo methodology) {
        Assert.isTrue(checkTitleUsable(methodology.title(),null),"Duplicated Methodology title" );
        EduMethodology eduMethodology = new EduMethodology();
        BeanUtils.copyProperties(methodology, eduMethodology);
        baseMapper.insert(eduMethodology);
        return eduMethodology.getId();
    }

    @Override
    public void updateMethodology(Long id, AdminMethodologyVo methodology) {

        EduMethodology eduMethodology = baseMapper.selectById(id);
        Assert.notNull(eduMethodology, "No corresponding methodology");
        Assert.isTrue(!ReviewStatus.APPLIED.equals(eduMethodology.getReview()),"Methodology is under review");
        Assert.isTrue(checkTitleUsable(methodology.title(),id),"Duplicated Methodology title" );
        BeanUtils.copyProperties(methodology, eduMethodology);
        baseMapper.updateById(eduMethodology);
    }

    @Override
    public PageResponse<EduMethodology> getMethodologyReviewPage(Integer current, Integer size) {

        LambdaQueryWrapper<EduMethodology> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduMethodology::getReview, ReviewStatus.APPLIED)
                .select(EduMethodology::getId, EduMethodology::getTitle, EduMethodology::getReview, EduMethodology::getIsModified
                        , EduMethodology::getLanguage, EduMethodology::getIsPublished);
        Page<EduMethodology> page = new Page<>(current, size);
        baseMapper.selectPage(page,wrapper);

        return PageUtil.toBean(page);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = FrontCacheConst.METHODOLOGY_NAME, key = "#id"),
            @CacheEvict(value = FrontCacheConst.METHODOLOGY_PAGE_NAME,allEntries = true) })
    public void switchEnableById(Long id) {
        EduMethodology eduMethodology = baseMapper.selectById(id);
        eduMethodology.setEnable(!eduMethodology.getEnable());
        baseMapper.updateById(eduMethodology);
    }

    @Override
    @Caching(evict = {@CacheEvict(value = FrontCacheConst.METHODOLOGY_NAME, key = "#id"),
            @CacheEvict(value = FrontCacheConst.METHODOLOGY_PAGE_NAME,allEntries = true) })
    public void switchPublicById(Long id) {
        EduMethodology eduMethodology = baseMapper.selectById(id);
        eduMethodology.setIsPublic(!eduMethodology.getIsPublic());
        baseMapper.updateById(eduMethodology);

    }

    @Override
    public PageResponse<EduMethodology> getMethodologyPage(PageQueryVo vo) {
        Page<EduMethodology> methodologyPage = new Page<>(vo.page(), vo.perPage());
        LambdaQueryWrapper<EduMethodology> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EduMethodology::getId, EduMethodology::getTitle,
                EduMethodology::getEnable,EduMethodology::getIsPublic,EduMethodology::getReview,
                EduMethodology::getIsPublished, EduMethodology::getIsModified, EduMethodology::getIsRemoveAfterReview);
        baseMapper.selectPage(methodologyPage, wrapper);
        return PageUtil.toBean(methodologyPage);
    }

    @Override
    @Transactional
    public void removeMethodologyById(Long id) {
        EduMethodology eduMethodology = baseMapper.selectById(id);
        Assert.notNull(eduMethodology,"Methodology no found");
        eduMethodology.setIsRemoveAfterReview(!eduMethodology.getIsRemoveAfterReview());
        baseMapper.updateById(eduMethodology);
    }

    @Override
    public Collection<? extends ReviewBasicDTO> getInfoByReviews(List<EduReview> reviews) {

        if(reviews.isEmpty()){
            return Collections.emptyList();
        }
        List<Long> methodologyIds= reviews.stream().map(EduReview::getRefId).collect(Collectors.toList());
        Map<Long, EduMethodology> methodologyMap = baseMapper.selectBatchIds(methodologyIds).stream().collect(Collectors.toMap(EduMethodology::getId, Function.identity()));
        return reviews.stream().map(review->ReviewBasicDTO.builder()
                .id(review.getId())
                .review(review.getStatus())
                .title(methodologyMap.get(review.getRefId()).getTitle())
                .gmtCreate(review.getGmtCreate())
                .refId(review.getRefId())
                .build()
        ).collect(Collectors.toList());
    }

    private boolean checkTitleUsable(@NotNull  String title, @Nullable Long id) {
        LambdaQueryWrapper<EduMethodology> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduMethodology::getTitle, title);
        wrapper.ne(id!=null, EduMethodology::getId, id);
        return !baseMapper.exists(wrapper);
    }
}
