package com.venson.changliulabstandalone.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.pojo.EduMethodology;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
import com.venson.changliulabstandalone.entity.enums.ReviewAction;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.statemachine.StateMachineConstant;
import com.venson.changliulabstandalone.mapper.EduMethodologyMapper;
import com.venson.changliulabstandalone.service.StateMachineService;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.CacheUtils;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;


@Service
public class MethodologyStateMachineConfig {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private TransactionTemplate transactionTemplate;
    @Autowired
    private StateMachineService stateMachineService;

    @Autowired
    private EduMethodologyMapper methodologyMapper;

    @Bean
    public StateMachine<ReviewStatus, ReviewAction, ReviewApplyVo> methodologyStateMachine() {
        StateMachineBuilder<ReviewStatus, ReviewAction, ReviewApplyVo> builder = StateMachineBuilderFactory.create();

        // request methodology review for new review
        builder.externalTransitions().fromAmong(ReviewStatus.NONE, ReviewStatus.FINISHED).to(ReviewStatus.APPLIED)
                .on(ReviewAction.REQUEST).when(requestNoneMethodologyCon())
                .perform(doNoneMethodologyRequest());

        // request methodology review for rejected review
        builder.externalTransition().from(ReviewStatus.REJECTED).to(ReviewStatus.APPLIED)
                .on(ReviewAction.REQUEST).when(requestRejectMethodologyCon())
                .perform(doRequestRejectMethodology());

        // pass methodology review
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.FINISHED)
                .on(ReviewAction.PASS).when(reviewMethodologyCon())
                .perform(doPassMethodology());

        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.REJECTED)
                .on(ReviewAction.REJECT).when(reviewMethodologyCon())
                .perform(doRejectMethodology());

        return builder.build(StateMachineConstant.METHODOLOGY_STATE_MACHINE_ID);
    }
    @PreAuthorize("hasAuthority('methodology.review.apply')")
    private Condition<ReviewApplyVo> requestNoneMethodologyCon() {
        return (ctx) -> {
            EduMethodology methodology = methodologyMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(methodology) && methodology.getReview() == ctx.getFrom();
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doNoneMethodologyRequest() {
        return (from, to, action, ctx) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                stateMachineService.requestNoneReview(ctx);

                LambdaUpdateWrapper<EduMethodology> wrapper = Wrappers.lambdaUpdate(EduMethodology.class);
                wrapper.eq(EduMethodology::getId,ctx.getId())
                        .set(EduMethodology::getReview, ReviewStatus.APPLIED);
                methodologyMapper.update(null, wrapper);

            }
        });
    }


    @PreAuthorize("hasAuthority('methodology.review.reject')")
    private Condition<ReviewApplyVo> requestRejectMethodologyCon() {
        return (ctx) -> {
            EduMethodology methodology = methodologyMapper.selectById(ctx.getId());
            Assert.notNull(methodology,"Methodology not exits");
            Assert.isTrue(methodology.getReview() == ctx.getFrom(), "Status error, Please retry");
            return true;
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doRequestRejectMethodology() {
        return (from, to, action, ctx) ->
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                        stateMachineService.requestRejectedReview(ctx);
                        LambdaUpdateWrapper<EduMethodology> wrapper = Wrappers.lambdaUpdate(EduMethodology.class);
                        wrapper.eq(EduMethodology::getId, ctx.getId()).eq(EduMethodology::getReview,ctx.getFrom())
                                .set(EduMethodology::getReview, ctx.getTo());
                        methodologyMapper.update(null, wrapper);

                    }
                });
//                methodologyService.requestRejectedMethodologyReview(ctx);
    }

    private Condition<ReviewApplyVo> reviewMethodologyCon() {
        return (ctx) -> {
            EduMethodology methodology = methodologyMapper.selectById(ctx.getId());
            Assert.notNull(methodology,"Methodology not exits");
            Assert.isTrue(methodology.getReview() == ctx.getFrom(), "Status error, Please retry");
            return true;
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doPassMethodology() {
        return (from, to, action, ctx) -> {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {

                    stateMachineService.doAlterReviewByCtx(ctx);
                    Long methodologyId =ctx.getId();
                    // remove methodology if marked isRemoveAfterReview
                    EduMethodology methodology = methodologyMapper.selectById(methodologyId);
                    if (methodology.getIsRemoveAfterReview()) {
                        methodologyMapper.deleteById(methodologyId);
                    } else {
                        // save or update published methodology.
                        methodology.setPublishedMd(methodology.getMarkdown());
                        methodology.setPublishedHtmlBrBase64(methodology.getHtmlBrBase64());
                        methodology.setReview(ReviewStatus.FINISHED);
                        methodology.setIsPublic(true);
                        methodology.setIsPublished(true);
                        methodologyMapper.updateById(methodology);
                        CacheUtils.evict(cacheManager, FrontCacheConst.METHODOLOGY_NAME, methodologyId.toString());
                        CacheUtils.evict(cacheManager, FrontCacheConst.METHODOLOGY_PAGE_NAME);
                    }
                }
            });

//            methodologyService.passMethodology(ctx);
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doRejectMethodology() {
        return (from, to, action, ctx) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {

                stateMachineService.doAlterReviewByCtx(ctx);
                // set review status to rejected and  isRemoveAfterReview to false,
                LambdaUpdateWrapper<EduMethodology> wrapper = Wrappers.lambdaUpdate(EduMethodology.class);
                wrapper.eq(EduMethodology::getId,ctx.getId())
                        .eq(EduMethodology::getReview, ReviewStatus.APPLIED)
                        .set(EduMethodology::getIsRemoveAfterReview, false)
                        .set(EduMethodology::getReview, ReviewStatus.REJECTED);
                methodologyMapper.update(null, wrapper);

            }
        });
//        methodologyService.rejectMethodology(ctx);
    }
}
