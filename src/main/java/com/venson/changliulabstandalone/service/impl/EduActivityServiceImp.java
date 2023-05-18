package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.dto.ActivityShowDTO;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import com.venson.changliulabstandalone.entity.pojo.*;
import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.PageUtil;
import com.venson.changliulabstandalone.entity.dto.ActivityAdminDTO;
import com.venson.changliulabstandalone.entity.dto.ActivityPreviewDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.mapper.EduActivityMapper;
import com.venson.changliulabstandalone.service.admin.EduActivityMarkdownService;
import com.venson.changliulabstandalone.service.admin.EduActivityPublishedMdService;
import com.venson.changliulabstandalone.service.admin.EduActivityPublishedService;
import com.venson.changliulabstandalone.service.admin.EduActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EduActivityServiceImp extends ServiceImpl<EduActivityMapper, EduActivity> implements EduActivityService {

    private final EduActivityMarkdownService markdownService;
    private final EduActivityPublishedService publishedService;
    private final EduActivityPublishedMdService publishedMdService;

    @Override
    public PageResponse<EduActivity> getPageReviewList(Integer page, Integer limit) {
        Page<EduActivity> reviewPage = new Page<>(page, limit);
        LambdaQueryWrapper<EduActivity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduActivity::getReview, ReviewStatus.APPLIED);
        baseMapper.selectPage(reviewPage, wrapper);
        return PageUtil.toBean(reviewPage);
    }


    @Override
    @Caching(evict = {@CacheEvict(value = FrontCacheConst.ACTIVITY_NAME,key = "#id"), @CacheEvict(value = FrontCacheConst.ACTIVITY_PAGE_NAME,allEntries = true)})
    public void switchEnableByActivityId(Long id) {
        EduActivity activity = baseMapper.selectById(id);
        Assert.notNull(activity, "Activity not exist");
        activity.setEnable(!activity.getEnable());
        baseMapper.updateById(activity);
        EduActivityPublished published = publishedService.getById(id);
        if (published != null) {
            published.setEnable(activity.getEnable());
            publishedService.updateById(published);
        }
    }

    @Override
    public PageResponse<EduActivity> getPageActivityList(Integer page, Integer limit, String title, String begin, String end) {
        LambdaQueryWrapper<EduActivity> wrapper = new QueryWrapper<EduActivity>().lambda();
        Page<EduActivity> pageActivity = new Page<>(page, limit);
        log.info("begin");
        log.info(begin);
        log.info("end");
        log.info(end);
        boolean beginEnable = StringUtils.hasText(begin);
        boolean endEnable = StringUtils.hasText(end);
        wrapper.select(EduActivity::getId, EduActivity::getTitle, EduActivity::getActivityDate, EduActivity::getAuthorMemberName
                , EduActivity::getIsModified, EduActivity::getIsPublished, EduActivity::getReview, EduActivity::getEnable);

        wrapper.like(StringUtils.hasText(title), EduActivity::getTitle, title);
        wrapper.ge(beginEnable, EduActivity::getGmtCreate, begin);
        wrapper.le(endEnable, EduActivity::getGmtCreate, end);
        wrapper.orderByDesc(EduActivity::getGmtCreate);
        baseMapper.selectPage(pageActivity, wrapper);
        return PageUtil.toBean(pageActivity);
    }


    @Transactional
    @Override
    public void updateActivity(Long id, ActivityAdminDTO activityAdminDTO) {
        EduActivity activity = baseMapper.selectById(id);
        Assert.notNull(activity, "Activity not exits");
        Assert.isTrue(activity.getReview() != ReviewStatus.APPLIED, "Activity is under Review");
        copyActivityBean(activityAdminDTO, activity);

//        BeanUtils.copyProperties(activityDTO, activity);
        activity.setIsModified(true);
        baseMapper.updateById(activity);

        EduActivityMarkdown markdown = markdownService.getById(id);
        markdown.setMarkdown(activityAdminDTO.getMarkdown());
        markdown.setHtmlBrBase64(activityAdminDTO.getHtmlBrBase64());
        markdownService.updateById(markdown);
    }

    @Override
    @Transactional
    public void deleteActivity(Long id) {
        EduActivity activity = baseMapper.selectById(id);
        if (activity.getReview() == null || activity.getReview() == ReviewStatus.NONE) {
            baseMapper.deleteById(id);
            markdownService.removeById(id);
        } else {
            activity.setIsRemoveAfterReview(true);
            baseMapper.updateById(activity);
        }
    }


    @Override
    public ActivityAdminDTO getActivityById(Long id) {

        EduActivity eduActivity = baseMapper.selectById(id);
        EduActivityMarkdown markdown = markdownService.getById(id);
        ActivityAdminDTO activity = new ActivityAdminDTO();
        BeanUtils.copyProperties(eduActivity, activity);
        activity.setMarkdown(markdown.getMarkdown());
        return activity;
    }

    @Override
    public ActivityPreviewDTO getPreviewByActivityId(long id) {
        EduActivity activity = baseMapper.selectById(id);
        ActivityPreviewDTO preview = new ActivityPreviewDTO();
        BeanUtils.copyProperties(activity, preview);
        EduActivityMarkdown markdown = markdownService.getById(id);
        EduActivityPublishedMd publishedMd = publishedMdService.getById(id);

        preview.setHtmlBrBase64(markdown.getHtmlBrBase64());
        if (!ObjectUtils.isEmpty(publishedMd)) preview.setPublishedHtmlBrBase64(publishedMd.getHtmlBrBase64());
        return preview;
    }

    @Override
    public Long addActivity(ActivityAdminDTO activityAdminDTO) {
        EduActivity eduActivity = new EduActivity();
        EduActivityMarkdown eduActivityMarkdown = new EduActivityMarkdown();
        copyActivityBean(activityAdminDTO, eduActivity);
        eduActivity.setReview(ReviewStatus.NONE);
        baseMapper.insert(eduActivity);
//        eduActivityMarkdown.setMarkdown(activityDTO.getMarkdown());
//        eduActivityMarkdown.setHtmlBrBase64(activityDTO.getHtmlBrBase64());
        eduActivityMarkdown.setId(eduActivity.getId());
        markdownService.save(eduActivityMarkdown);
        return eduActivity.getId();
    }

    @Override
    public PageResponse<EduActivity> getPageActivityList(PageQueryVo vo) {
            Page<EduActivity> reviewPage = new Page<>(vo.page(),vo.perPage());
            LambdaQueryWrapper<EduActivity> wrapper = new LambdaQueryWrapper<>();
//            wrapper.eq(EduActivity::getReview, ReviewStatus.APPLIED);
        wrapper.select(EduActivity::getId,EduActivity::getTitle,EduActivity::getIsRemoveAfterReview
                , EduActivity::getEnable, EduActivity::getActivityDate, EduActivity::getIsPublished
                , EduActivity::getIsModified, EduActivity::getReview);
            baseMapper.selectPage(reviewPage,wrapper);
            return PageUtil.toBean(reviewPage);
    }

    @Override
    public Map<Long, String> getIdTitleMap(List<Long> refIds) {
        LambdaQueryWrapper<EduActivity> wrapper = Wrappers.lambdaQuery();
        wrapper.select(EduActivity::getTitle, EduActivity::getId).in(EduActivity::getId,refIds);
        List<EduActivity> activities = baseMapper.selectList(wrapper);
        return activities.stream().collect(Collectors.toMap(EduActivity::getId, EduActivity::getTitle));
    }

    @Override
    public List<ReviewBasicDTO> getInfoByReviews(List<EduReview> reviews) {
        if(reviews.isEmpty()){
            return Collections.emptyList();
        }
        List<Long> activityIds = reviews.stream().map(EduReview::getRefId).collect(Collectors.toList());
        Map<Long, EduActivity> activityMap = baseMapper.selectBatchIds(activityIds).stream().collect(Collectors.toMap(EduActivity::getId, Function.identity()));
        return reviews.stream().map(review->ReviewBasicDTO.builder()
                .id(review.getId())
                .review(review.getStatus())
                .title(activityMap.get(review.getRefId()).getTitle())
                .gmtCreate(review.getGmtCreate())
                .refId(review.getRefId())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public ReviewAble getReviewById(Long refId) {
        EduActivity eduActivity = baseMapper.selectById(refId);
        EduActivityMarkdown markdown = markdownService.getById(refId);
        ActivityShowDTO activityShowDTO = new ActivityShowDTO();
        activityShowDTO.setDate(eduActivity.getActivityDate());
        activityShowDTO.setHtml(markdown.getHtmlBrBase64());
        activityShowDTO.setTitle(eduActivity.getTitle());
        return activityShowDTO;
    }

    private void copyActivityBean(ActivityAdminDTO source, EduActivity target) {
        target.setTitle(source.getTitle());
        target.setActivityDate(source.getActivityDate());
        target.setAuthorMemberId(source.getAuthorMemberId());
        target.setAuthorMemberName(source.getAuthorMemberName());

    }
}
