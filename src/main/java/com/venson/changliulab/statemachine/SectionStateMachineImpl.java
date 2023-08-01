package com.venson.changliulab.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.enums.PermissionAction;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import com.venson.changliulab.service.StateMachineService;
import com.venson.changliulab.service.admin.EduSectionService;
import com.venson.changliulab.utils.CacheUtils;
import com.venson.changliulab.utils.ContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Consumer;


@Configuration
@RequiredArgsConstructor
public class SectionStateMachineImpl implements ReviewStateMachine {

    private final EduSectionService sectionService;

    private final TransactionTemplate transactionTemplate;
    private final StateMachineService stateMachineService;
    private final CacheManager cacheManager;



    public Condition<ReviewSMContext> checkRequestNew() {
        return (ctx) -> ContextUtils.hasAuthority("Course", PermissionAction.REVIEW_REQUEST) &&
                sectionService.exitsByIds(ctx.ids(), ReviewStatus.NONE, ReviewStatus.FINISHED);
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestNew() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.requestReviews(ctx);
            sectionService.updateReviewStatusByIds(ctx.ids(), ReviewStatus.APPLIED);
        });
    }


    @Override
    public Condition<ReviewSMContext> checkRequestRejected() {
        return (ctx) -> ContextUtils.hasAuthority("Course", PermissionAction.REVIEW_REQUEST) &&
         sectionService.exitsByIds(ctx.ids(), ReviewStatus.REJECTED);
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestRejected() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.requestRejectedReview(ctx);
            sectionService.updateReviewStatusByIds(ctx.ids(), ReviewStatus.APPLIED);
        });
    }

    @Override
    public Condition<ReviewSMContext> checkPass() {
        return (ctx) -> ContextUtils.hasAuthority("Course", PermissionAction.REVIEW_PASS) &&
         sectionService.exitsByIds(ctx.ids(),ReviewStatus.APPLIED);
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doPass() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.passReviews(ctx);
            sectionService.updateReviewStatusByIds(ctx.ids(), ReviewStatus.FINISHED);
            sectionService.publishReviewedSection(ctx.ids());
            Long courseId = sectionService.getCourseIdById(ctx.ids().get(0));
            CacheUtils.evict(cacheManager,FrontCacheConst.SECTION_NAME,ctx.ids());
            CacheUtils.evict(cacheManager,FrontCacheConst.COURSE_NAME,
                    CacheUtils.getPrefix(FrontCacheConst.COURSE_TOC_KEY_PREFIX)+ courseId,
                    CacheUtils.getPrefix(FrontCacheConst.COURSE_SYLLABUS_KEY_PREFIX)+ courseId);

        });

    }

    @Override
    public Condition<ReviewSMContext> checkReject() {

        return (ctx) -> ContextUtils.hasAuthority("Course", PermissionAction.REVIEW_REJECT) &&
                sectionService.exitsByIds(ctx.ids(),ReviewStatus.APPLIED);
    }
    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doReject() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.rejectReviews(ctx);
            sectionService.updateReviewStatusByIds(ctx.ids(),ReviewStatus.REJECTED);
        });
    }
}
