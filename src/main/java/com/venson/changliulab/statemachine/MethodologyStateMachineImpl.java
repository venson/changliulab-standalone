package com.venson.changliulab.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.venson.changliulab.entity.enums.PermissionAction;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.statemachine.StateMachineConstant;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import com.venson.changliulab.service.StateMachineService;
import com.venson.changliulab.service.admin.EduMethodologyService;
import com.venson.changliulab.utils.ContextUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


@Service
@RequiredArgsConstructor
public class MethodologyStateMachineImpl implements ReviewStateMachine {

    private final CacheManager cacheManager;

    private final EduMethodologyService methodologyService;
    private final TransactionTemplate transactionTemplate;
    private final StateMachineService stateMachineService;



    @Override
    public Condition<ReviewSMContext> checkRequestNew() {
    return (ctx) -> ContextUtils.hasAuthority("Methodology", PermissionAction.REVIEW_REQUEST) &&
            methodologyService.existsByIds(ctx.ids(), ReviewStatus.APPLIED);

    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestNew() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult((status)-> {
                stateMachineService.requestReviews(ctx);
                methodologyService.updateReviewStatusByIds(ctx.ids(), ReviewStatus.APPLIED);

        });
    }


    @Override
    public Condition<ReviewSMContext> checkRequestRejected() {
        return (ctx) -> ContextUtils.hasAuthority("Methodology", PermissionAction.REVIEW_REQUEST) &&
        methodologyService.existsByIds(ctx.ids(), ReviewStatus.APPLIED);
    }
    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestRejected() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.requestRejectedReview(ctx);
            methodologyService.updateReviewStatusByIds(ctx.ids(), ReviewStatus.APPLIED);
        });
    }

    @Override
    public Condition<ReviewSMContext> checkReject() {
        return (ctx) -> ContextUtils.hasAuthority("Methodology", PermissionAction.REVIEW_REJECT) &&
             methodologyService.existsByIds(ctx.ids(), ReviewStatus.APPLIED);
    }
    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doReject() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(status ->{
            stateMachineService.rejectReviews(ctx);
            methodologyService.updateReviewStatusByIds(ctx.ids(), ReviewStatus.REJECTED);
        });
    }

    @Override
    public Condition<ReviewSMContext> checkPass() {
        return (ctx) -> ContextUtils.hasAuthority("Methodology", PermissionAction.REVIEW_PASS) &&
         methodologyService.existsByIds(ctx.ids(), ReviewStatus.APPLIED);
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doPass() {
        return (from, to, action, ctx) -> transactionTemplate.executeWithoutResult(status ->{
            stateMachineService.passReviews(ctx);
            methodologyService.publishReviewedMethodology(ctx.ids());

        }) ;
    }



}
