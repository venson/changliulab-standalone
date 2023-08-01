package com.venson.changliulab.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.enums.PermissionAction;
import com.venson.changliulab.entity.pojo.EduReport;
import com.venson.changliulab.entity.pojo.EduReportMarkdown;
import com.venson.changliulab.entity.pojo.EduReportPublished;
import com.venson.changliulab.entity.pojo.EduReportPublishedMd;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.statemachine.StateMachineConstant;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import com.venson.changliulab.mapper.EduReportMapper;
import com.venson.changliulab.mapper.EduReportMarkdownMapper;
import com.venson.changliulab.mapper.EduReportPublishedMapper;
import com.venson.changliulab.mapper.EduReportPublishedMdMapper;
import com.venson.changliulab.service.EduReportService;
import com.venson.changliulab.service.StateMachineService;
import com.venson.changliulab.utils.CacheUtils;
import com.venson.changliulab.utils.ContextUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class ReportStateMachineImpl implements ReviewStateMachine {

    private final TransactionTemplate transactionTemplate;
    private final StateMachineService stateMachineService;
    private final EduReportService reportService;

    final
    CacheManager cacheManager;

    @Override
    public Condition<ReviewSMContext> checkRequestNew() {
        return (ctx) -> ContextUtils.hasAuthority("Report", PermissionAction.REVIEW_REQUEST) &&
                reportService.existsByIds(ctx.ids(),ReviewStatus.NONE,ReviewStatus.FINISHED);
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestNew() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.requestReviews(ctx);
            reportService.updateReviewedReport(ctx.ids(),ReviewStatus.APPLIED);
        }) ;
    }


    @Override
    public Condition<ReviewSMContext> checkRequestRejected() {
        return (ctx) -> ContextUtils.hasAuthority("Report", PermissionAction.REVIEW_REQUEST) &&
         reportService.existsByIds(ctx.ids(),ReviewStatus.REJECTED);
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestRejected() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.requestRejectedReview(ctx);
            reportService.updateReviewedReport(ctx.ids(),ReviewStatus.APPLIED);
        });

    }

    @Override
    public Condition<ReviewSMContext> checkPass() {
        return (ctx) -> ContextUtils.hasAuthority("Report", PermissionAction.REVIEW_PASS) &&
        reportService.existsByIds(ctx.ids(), ReviewStatus.APPLIED) ;
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doPass() {
        return (from, to, action, ctx) -> {
            stateMachineService.passReviews(ctx);
            reportService.publishReviewedReport(ctx.ids());
            reportService.updateReviewedReport(ctx.ids(),ReviewStatus.FINISHED);
            CacheUtils.evict(cacheManager,FrontCacheConst.REPORT_NAME,ctx.ids());
            CacheUtils.evict(cacheManager,FrontCacheConst.REPORT_PAGE_NAME);
        };
    }
    @Override
    public Condition<ReviewSMContext> checkReject() {
        return (ctx) -> ContextUtils.hasAuthority("Report", PermissionAction.REVIEW_REJECT) &&
         reportService.existsByIds(ctx.ids(), ReviewStatus.APPLIED) ;
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doReject() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.rejectReviews(ctx);
            reportService.updateReviewedReport(ctx.ids(),ReviewStatus.REJECTED);
        });
    }
}
