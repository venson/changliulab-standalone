package com.venson.changliulab.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.enums.PermissionAction;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import com.venson.changliulab.service.StateMachineService;
import com.venson.changliulab.service.admin.EduCourseService;
import com.venson.changliulab.utils.CacheUtils;
import com.venson.changliulab.utils.ContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.swing.text.Utilities;

@Configuration
@RequiredArgsConstructor
public class CourseStateMachineImpl implements ReviewStateMachine{

    private final StateMachineService stateMachineService;
    private final CacheManager cacheManager;
    private final EduCourseService courseService;
    private final TransactionTemplate transactionTemplate;



    // request for course info
    @Override
    public Condition<ReviewSMContext> checkRequestNew() {

        return (ctx) -> ContextUtils.hasAuthority("Course", PermissionAction.REVIEW_REQUEST) &&
                courseService.exitsByIds(ctx.ids(), ReviewStatus.FINISHED, ReviewStatus.NONE);
    }
    @Override
    public Action<ReviewStatus, ReviewAction,ReviewSMContext> doRequestNew(){
        return (from, to, event, context)-> transactionTemplate.executeWithoutResult(  status ->{
            stateMachineService.requestReviews(context);
            courseService.updateReviewStatusByCourseIds(context.ids(), ReviewStatus.APPLIED);
        });
    }


    @Override
    public Condition<ReviewSMContext> checkRequestRejected() {
        return (ctx) -> ContextUtils.hasAuthority("Course", PermissionAction.REVIEW_REQUEST) &&
         courseService.exitsByIds(ctx.ids(), ReviewStatus.REJECTED);
    }
    @Override
    public Action<ReviewStatus, ReviewAction,ReviewSMContext> doRequestRejected(){
        return (from, to, event, context)->  transactionTemplate.executeWithoutResult(transactionStatus ->{
            stateMachineService.requestRejectedReview(context);
            courseService.updateReviewStatusByCourseIds(context.ids(), ReviewStatus.APPLIED);
        });

    }

    /**
     * Condition check for pass course action;
     * @return state machine condition
     */
    @Override
    public Condition<ReviewSMContext> checkPass() {
        return (ctx) -> ContextUtils.hasAuthority("Course", PermissionAction.REVIEW_PASS) &&
          courseService.exitsByIds(ctx.ids(),ReviewStatus.APPLIED);
    }
    @Override
    public Action<ReviewStatus, ReviewAction,ReviewSMContext> doPass(){
        return (from, to, event, context)->  transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.passReviews(context);
            courseService.publishReviewedCourse(context.ids());
            courseService.updateReviewStatusByCourseIds(context.ids(),ReviewStatus.FINISHED);
            CacheUtils.evict(cacheManager,FrontCacheConst.COURSE_NAME, context.ids() );
        });
    }
    @Override
    public Condition<ReviewSMContext> checkReject() {
        return (ctx) -> ContextUtils.hasAuthority("Course", PermissionAction.REVIEW_REJECT) &&
         courseService.exitsByIds(ctx.ids(),ReviewStatus.APPLIED);
    }
    @Override
    public Action<ReviewStatus, ReviewAction,ReviewSMContext> doReject(){
        return (from, to, event, context)->  transactionTemplate.executeWithoutResult(transactionStatus -> {
            stateMachineService.rejectReviews(context);
            courseService.updateReviewStatusByCourseIds(context.ids(),ReviewStatus.REJECTED);
        });
    }







}
