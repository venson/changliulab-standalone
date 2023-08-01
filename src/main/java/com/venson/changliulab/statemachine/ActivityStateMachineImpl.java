package com.venson.changliulab.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.enums.PermissionAction;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import com.venson.changliulab.service.StateMachineService;
import com.venson.changliulab.service.admin.EduActivityService;
import com.venson.changliulab.utils.CacheUtils;
import com.venson.changliulab.utils.ContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;


@Component
@Slf4j
@RequiredArgsConstructor
public class ActivityStateMachineImpl implements ReviewStateMachine{

    private final TransactionTemplate transactionTemplate;
    private final StateMachineService stateMachineService;
    private final EduActivityService activityService;

    private final CacheManager cacheManager;


    @Override
    public Condition<ReviewSMContext> checkRequestNew() {
        return (ctx) -> ContextUtils.hasAuthority("Activity", PermissionAction.REVIEW_REQUEST) &&
                activityService.existsByIds(ctx.ids());
    }

    @Override
    public Action<ReviewStatus, ReviewAction,ReviewSMContext> doRequestNew() {
        return (from, to, action, ctx) ->
            transactionTemplate.executeWithoutResult(transactionStatus ->  {
                    stateMachineService.requestReviews(ctx);
                    activityService.updateReviewsByIds(ctx.ids(), ReviewStatus.APPLIED);

            }
        );
    }


    @Override
    public Condition<ReviewSMContext> checkRequestRejected() {
        return (ctx) -> ContextUtils.hasAuthority("Activity", PermissionAction.REVIEW_REQUEST) &&
         activityService.existsByIds(ctx.ids());
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestRejected() {
        return (from, to, action, ctx) ->
                transactionTemplate.executeWithoutResult(status-> {
                        stateMachineService.requestRejectedReview(ctx);
                        activityService.updateReviewsByIds(ctx.ids(),ReviewStatus.APPLIED);
                }
        );
//                activityService.requestRejectedActivityReview(ctx);
    }

    @Override
    public Condition<ReviewSMContext> checkPass() {
        return (ctx) -> ContextUtils.hasAuthority("Activity", PermissionAction.REVIEW_PASS) &&
         activityService.existsByIds(ctx.ids());
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doPass() {
        return (from, to, action, ctx) -> {
            transactionTemplate.executeWithoutResult(transactionStatus ->  {
                    stateMachineService.passReviews(ctx);
                    activityService.publishActivities(ctx);
                    CacheUtils.evict(cacheManager,FrontCacheConst.ACTIVITY_NAME, ctx.ids());
                    CacheUtils.evict(cacheManager,FrontCacheConst.ACTIVITY_PAGE_NAME);

            });

//            activityService.passActivity(ctx);
        };
    }
    @Override
    public Condition<ReviewSMContext> checkReject() {
        return (ctx) -> ContextUtils.hasAuthority("Activity", PermissionAction.REVIEW_REJECT) &&
         activityService.existsByIds(ctx.ids());
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doReject() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.rejectReviews(ctx);
            // set review status to rejected and  isRemoveAfterReview to false,
            activityService.updateReviewsByIds(ctx.ids(), ReviewStatus.REJECTED);
        });
    }
}
