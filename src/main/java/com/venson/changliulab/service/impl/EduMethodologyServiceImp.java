package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.dto.*;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.*;
import com.venson.changliulab.entity.vo.admin.AdminMethodologyVo;
import com.venson.changliulab.entity.vo.admin.CommonMetaVo;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.mapper.EduMethodologyMapper;
import com.venson.changliulab.service.admin.EduMethodologyPublishedService;
import com.venson.changliulab.service.admin.EduMethodologyService;
import com.venson.changliulab.service.admin.ReviewableService;
import com.venson.changliulab.utils.Assert;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.PageUtil;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class EduMethodologyServiceImp extends ServiceImpl<EduMethodologyMapper, EduMethodology> implements EduMethodologyService, ReviewableService {

    private final EduMethodologyPublishedService publishedService;
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
    public MethodologyPreviewDTO getMethodologyViewById(Long id) {

        EduMethodology eduMethodology = baseMapper.selectById(id);
        return MethodologyPreviewDTO.builder()
                .id(eduMethodology.getId())
                .title(eduMethodology.getTitle())
                .htmlBrBase64(eduMethodology.getHtmlBrBase64())
                .isModified(eduMethodology.getIsModified())
                .isPublished(eduMethodology.getIsPublished())
                .review(eduMethodology.getReview())
                .build();
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

                .type(review.getRefType())
                .gmtCreate(review.getGmtCreate())
                .refId(review.getRefId())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public ReviewAble getReviewById(Long refId) {
        EduMethodology eduMethodology = baseMapper.selectById(refId);

        Assert.notNull(eduMethodology,"Methodology no found");
        return ShowMethodologyDTO.builder().isPublic(eduMethodology.getIsPublic())
                .html(eduMethodology.getHtmlBrBase64())
                .title(eduMethodology.getTitle()).build();
    }

    @Override
    public ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id) {
        EduMethodology methodology= baseMapper.selectById(id);
        Assert.notNull(methodology,"Methodology Not Found");
        EduMethodologyPublished published= publishedService.getById(id);

        ShowMethodologyDTO applied= ShowMethodologyDTO.builder()
                .html(methodology.getHtmlBrBase64())
                .title(methodology.getTitle()).build();
        ShowMethodologyDTO reviewed= null;
        if(published!=null){
            reviewed = ShowMethodologyDTO.builder()
                    .html(published.getHtmlBrBase64())
                    .title(published.getTitle()).build();
        }



        return ReviewTargetDTO.<ReviewAble>builder()
                .applied(applied)
                .reviewed(reviewed)
                .status(methodology.getReview())
                .title(methodology.getTitle())
                .id(methodology.getId())
                .build();
    }

    @Override
    public boolean existsByIds(List<Long> ids, ReviewStatus status) {
        if(ids.isEmpty()) return false;
        LambdaQueryWrapper<EduMethodology> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(EduMethodology::getId, ids)
                .eq(EduMethodology::getReview, status);
        return baseMapper.selectCount(wrapper).intValue() ==ids.size();
    }

    @Override
    public void updateReviewStatusByIds(List<Long> ids, ReviewStatus reviewStatus) {
        LambdaUpdateWrapper<EduMethodology> wrapper = Wrappers.lambdaUpdate();
        wrapper.in(EduMethodology::getId, ids);
        wrapper.set(EduMethodology::getReview, reviewStatus);
        baseMapper.update(null, wrapper);

    }

    @Override
    public void publishReviewedMethodology(List<Long> ids) {

        LambdaQueryWrapper<EduMethodology> wrapper = Wrappers.lambdaQuery();
        wrapper.in(EduMethodology::getId, ids);
//        wrapper.select(EduMethodology::getId, EduMethodology::getHtmlBrBase64, EduMethodology::getTitle);
        List<EduMethodology> methodologies = baseMapper.selectList(wrapper);
        List<EduMethodologyPublished> publishedList = methodologies.stream().map(methodology -> {
            EduMethodologyPublished published = new EduMethodologyPublished();
            BeanUtils.copyProperties(methodology, published);
            return published;
        }).collect(Collectors.toList());
        publishedService.saveOrUpdateBatch(publishedList);

    }

    @Override
    public EduMethodology getMethodologyById(Long id, CommonMetaVo vo) {
        LambdaQueryWrapper<EduMethodology> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(EduMethodology::getId, id);

        if (vo !=null && "preview".equals(vo.type())){
            wrapper.select(EduMethodology::getId, EduMethodology::getHtmlBrBase64,
                    EduMethodology::getIsModified, EduMethodology::getTitle,
                    EduMethodology::getIsPublic,
                    EduMethodology::getReview, EduMethodology::getIsRemoveAfterReview);
        }else{
            wrapper.select(EduMethodology::getId,EduMethodology::getIsModified,
                    EduMethodology::getMarkdown,EduMethodology::getTitle,
                    EduMethodology::getIsPublic,EduMethodology::getReview,
                    EduMethodology::getIsRemoveAfterReview);
        }

        return baseMapper.selectOne(wrapper);
    }

    private boolean checkTitleUsable(@NotNull  String title, @Nullable Long id) {
        LambdaQueryWrapper<EduMethodology> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduMethodology::getTitle, title);
        wrapper.ne(id!=null, EduMethodology::getId, id);
        return !baseMapper.exists(wrapper);
    }
}
