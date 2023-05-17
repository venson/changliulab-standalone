package com.venson.changliulabstandalone.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.EduReport;
import com.venson.changliulabstandalone.entity.EduReportMarkdown;
import com.venson.changliulabstandalone.entity.EduReportPublished;
import com.venson.changliulabstandalone.entity.EduReportPublishedMd;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
import com.venson.changliulabstandalone.entity.enums.ReviewAction;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.statemachine.StateMachineConstant;
import com.venson.changliulabstandalone.mapper.EduReportMapper;
import com.venson.changliulabstandalone.mapper.EduReportMarkdownMapper;
import com.venson.changliulabstandalone.mapper.EduReportPublishedMapper;
import com.venson.changliulabstandalone.mapper.EduReportPublishedMdMapper;
import com.venson.changliulabstandalone.service.StateMachineService;
import com.venson.changliulabstandalone.utils.CacheUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;


@Configuration
@Slf4j
@RequiredArgsConstructor
public class ReportStateMachineImpl {

    private final TransactionTemplate transactionTemplate;
    private final StateMachineService stateMachineService;

    final
    CacheManager cacheManager;
    private final EduReportMapper reportMapper;
    private final EduReportMarkdownMapper reportMarkdownMapper;
    private final EduReportPublishedMapper reportPublishedMapper;
    private final EduReportPublishedMdMapper reportPublishedMdMapper;


    @Bean
    public StateMachine<ReviewStatus, ReviewAction, ReviewApplyVo> reportStateMachine() {
        StateMachineBuilder<ReviewStatus, ReviewAction, ReviewApplyVo> builder = StateMachineBuilderFactory.create();

        // request report review for new review
        builder.externalTransitions().fromAmong(ReviewStatus.NONE, ReviewStatus.FINISHED).to(ReviewStatus.APPLIED)
                .on(ReviewAction.REQUEST).when(requestNoneReportCon())
                .perform(doNoneReportRequest());

        // request report review for rejected review
        builder.externalTransition().from(ReviewStatus.REJECTED).to(ReviewStatus.APPLIED)
                .on(ReviewAction.REQUEST).when(requestRejectReportCon())
                .perform(doRequestRejectReport());

        // pass report review
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.FINISHED)
                .on(ReviewAction.PASS).when(reviewReportCon())
                .perform(doPassReport());

        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.REJECTED)
                .on(ReviewAction.REJECT).when(reviewReportCon())
                .perform(doRejectReport());

        return builder.build(StateMachineConstant.REPORT_STATE_MACHINE_ID);
    }
    @PreAuthorize("hasAuthority('report.review.apply')")
    private Condition<ReviewApplyVo> requestNoneReportCon() {
        return (ctx) -> {
            EduReport report = reportMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(report) && report.getReview() == ctx.getFrom();
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doNoneReportRequest() {
        return (from, to, action, ctx) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                stateMachineService.requestNoneReview(ctx);

                LambdaUpdateWrapper<EduReport> wrapper = Wrappers.lambdaUpdate(EduReport.class);
                wrapper.eq(EduReport::getId,ctx.getId())
                        .set(EduReport::getReview, ReviewStatus.APPLIED);
                reportMapper.update(null, wrapper);

            }
        });
    }


    @PreAuthorize("hasAuthority('report.review.reject')")
    private Condition<ReviewApplyVo> requestRejectReportCon() {
        return (ctx) -> {
            EduReport report = reportMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(report) && report.getReview() == ctx.getFrom();
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doRequestRejectReport() {
        return (from, to, action, ctx) ->
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                        stateMachineService.requestRejectedReview(ctx);
                        LambdaUpdateWrapper<EduReport> wrapper = Wrappers.lambdaUpdate(EduReport.class);
                        wrapper.eq(EduReport::getId, ctx.getId()).eq(EduReport::getReview,ctx.getFrom())
                                .set(EduReport::getReview, ctx.getTo());
                        reportMapper.update(null, wrapper);

                    }
                });
//                reportService.requestRejectedReportReview(ctx);
    }

    private Condition<ReviewApplyVo> reviewReportCon() {
        return (ctx) -> {
            EduReport report = reportMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(report) && report.getReview() == ctx.getFrom();
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doPassReport() {
        return (from, to, action, ctx) -> {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {

                    stateMachineService.doAlterReviewByCtx(ctx);
                    Long reportId =ctx.getId();
                    // remove report if marked isRemoveAfterReview
                    EduReport report = reportMapper.selectById(reportId);
                    if (report.getIsRemoveAfterReview()) {
                        reportMapper.deleteById(reportId);
                        reportMarkdownMapper.deleteById(reportId);
                        reportPublishedMapper.deleteById(reportId);
                        reportPublishedMdMapper.deleteById(reportId);
                    } else {
                        report.setReview(ctx.getTo());
                        report.setIsPublished(true);
                        report.setEnable(true);
                        reportMapper.updateById(report);
                        // save or update published report.
                        EduReportMarkdown reportMarkdown = reportMarkdownMapper.selectById(reportId);
                        EduReportPublished published = reportPublishedMapper.selectById(reportId);
                        EduReportPublishedMd publishedMd = reportPublishedMdMapper.selectById(reportId);
                        if (ObjectUtils.isEmpty(published)) {
                            published = new EduReportPublished();
                            publishedMd = new EduReportPublishedMd();
                            BeanUtils.copyProperties(report, published);
                            BeanUtils.copyProperties(reportMarkdown, publishedMd);
                            reportPublishedMapper.insert(published);
                            reportPublishedMdMapper.insert(publishedMd);
                        } else {
                            BeanUtils.copyProperties(report, published);
                            BeanUtils.copyProperties(reportMarkdown, publishedMd);
                            reportPublishedMapper.updateById(published);
                            reportPublishedMdMapper.updateById(publishedMd);

                        }
                        CacheUtils.evict(cacheManager,FrontCacheConst.REPORT_NAME,reportId.toString());
                        CacheUtils.evict(cacheManager,FrontCacheConst.REPORT_PAGE_NAME);

                    }
                }
            });

//            reportService.passReport(ctx);
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doRejectReport() {
        return (from, to, action, ctx) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {

                stateMachineService.doAlterReviewByCtx(ctx);
                // set review status to rejected and  isRemoveAfterReview to false,
                LambdaUpdateWrapper<EduReport> wrapper = Wrappers.lambdaUpdate(EduReport.class);
                wrapper.eq(EduReport::getId,ctx.getId())
                        .eq(EduReport::getReview, ReviewStatus.APPLIED)
                        .set(EduReport::getIsRemoveAfterReview, false)
                        .set(EduReport::getReview, ReviewStatus.REJECTED);
                reportMapper.update(null, wrapper);

            }
        });
//        reportService.rejectReport(ctx);
    }
}
