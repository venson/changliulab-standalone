package com.venson.changliulab.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.statemachine.StateMachineConstant;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import com.venson.changliulab.service.*;
import com.venson.changliulab.service.admin.EduChapterService;
import com.venson.changliulab.utils.CacheUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;


@Service
@RequiredArgsConstructor
public class ChapterStateMachineImpl implements ReviewStateMachine {

    private final StateMachineService stateMachineService;
    private final TransactionTemplate transactionTemplate;
    private final EduChapterService chapterService;
    private final CacheManager cacheManager;


    @Override
    public Condition<ReviewSMContext> checkRequestNew() {
        return (ctx) -> {
            return chapterService.existsByIds(ctx.ids());
        };
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestNew() {
        return (from, to, event,ctx) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                stateMachineService.requestReviews(ctx);
                chapterService.updateReviewStatusByIds(ctx.ids(),ReviewStatus.APPLIED);
            }
        });
    }


    @Override
    public Condition<ReviewSMContext> checkRequestRejected() {
        return (ctx) -> chapterService.existsByIds(ctx.ids());
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestRejected() {
        return (from, to, event, context) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                stateMachineService.requestRejectedReview(context);
                chapterService.updateReviewStatusByIds(context.ids(), ReviewStatus.APPLIED);
            }
        });
    }


    @Override
    public Condition<ReviewSMContext> checkPass() {
        return (ctx) -> chapterService.existsByIds(ctx.ids());
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doPass() {
        return (from, to, event, context) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {

                stateMachineService.passReviews(context);
                chapterService.updateReviewStatusByIds(context.ids(), ReviewStatus.FINISHED);
                chapterService.publishReviewedChapters(context.ids());
                    CacheUtils.evict(cacheManager,FrontCacheConst.COURSE_NAME,
                            CacheUtils.getPrefix(FrontCacheConst.COURSE_TOC_KEY_PREFIX)+ chapterService.getCourseId(context),
                            CacheUtils.getPrefix(FrontCacheConst.COURSE_SYLLABUS_KEY_PREFIX)+ chapterService.getCourseId(context)
                    );
            }
        });
    }

//    @PreAuthorize("hasAuthority('course.review.reject')")
    @Override
    public Condition<ReviewSMContext> checkReject() {
        return (ctx) -> chapterService.existsByIds(ctx.ids());
    }

    @Override
    public Action<ReviewStatus, ReviewAction, ReviewSMContext> doReject() {
        return (from, to, event, context) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                stateMachineService.rejectReviews(context);
                chapterService.updateReviewStatusByIds(context.ids(),ReviewStatus.REJECTED);
            }
        });
    }







}
