package com.venson.changliulab.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.venson.changliulab.entity.enums.PermissionAction;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import com.venson.changliulab.service.StateMachineService;
import com.venson.changliulab.service.admin.EduResearchService;
import com.venson.changliulab.utils.ContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.awt.color.ICC_ColorSpace;


@Configuration
@RequiredArgsConstructor
public class ResearchStateMachineImpl implements ReviewStateMachine {

    private final EduResearchService researchService;
    private final StateMachineService stateMachineService;
    private final TransactionTemplate transactionTemplate;
    private final CacheManager cacheManager;




    @Override
    public Condition<ReviewSMContext>checkRequestNew() {

        return (ctx) -> ContextUtils.hasAuthority("Research", PermissionAction.REVIEW_REQUEST) &&
                researchService.existsByIds(ctx.ids(),ReviewStatus.FINISHED,ReviewStatus.NONE );
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestNew() {
        return (from, to, action, ctx) ->  transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.requestReviews(ctx);
            researchService.updateReviewedResearch(ctx.ids(), ReviewStatus.APPLIED);
        });
    }


    @Override
    public Condition<ReviewSMContext> checkRequestRejected() {
        return (ctx) -> ContextUtils.hasAuthority("Research", PermissionAction.REVIEW_REQUEST) &&
         researchService.existsByIds(ctx.ids(), ReviewStatus.REJECTED);
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestRejected() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(transactionStatus -> {

                    stateMachineService.requestRejectedReview(ctx);
                    researchService.updateReviewedResearch(ctx.ids(), ReviewStatus.APPLIED);
                });
    }

    @Override
    public Condition<ReviewSMContext> checkPass() {
        return (ctx) -> ContextUtils.hasAuthority("Research", PermissionAction.REVIEW_PASS) &&
         researchService.existsByIds(ctx.ids(), ReviewStatus.APPLIED);
    }
    @Override
    public Condition<ReviewSMContext> checkReject() {
        return (ctx) -> ContextUtils.hasAuthority("Research", PermissionAction.REVIEW_REJECT) &&
         researchService.existsByIds(ctx.ids(), ReviewStatus.APPLIED);
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doPass() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(transactionStatus -> {
                stateMachineService.passReviews(ctx);
                researchService.publishReviewedResearch(ctx.ids());
                researchService.updateReviewedResearch(ctx.ids(),ReviewStatus.APPLIED);
            });
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doReject() {
        return (from, to, action, ctx) ->  transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.rejectReviews(ctx);
            researchService.updateReviewedResearch(ctx.ids(),ReviewStatus.REJECTED);
        });
    }
}
