package com.venson.changliulabstandalone.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
import com.venson.changliulabstandalone.entity.enums.ReviewAction;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.pojo.EduSection;
import com.venson.changliulabstandalone.entity.pojo.EduSectionMarkdown;
import com.venson.changliulabstandalone.entity.pojo.EduSectionPublished;
import com.venson.changliulabstandalone.entity.pojo.EduSectionPublishedMd;
import com.venson.changliulabstandalone.entity.statemachine.StateMachineConstant;
import com.venson.changliulabstandalone.mapper.*;
import com.venson.changliulabstandalone.service.*;
import com.venson.changliulabstandalone.utils.CacheUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class SectionStateMachineImpl {


    private final TransactionTemplate transactionTemplate;
    private final EduSectionMarkdownMapper markdownMapper;

    private final EduSectionPublishedMapper publishedMapper;
    private final EduSectionPublishedMdMapper publishedMdMapper;

    private final EduSectionMapper sectionMapper;

    private final StateMachineService stateMachineService;
    private final CacheManager cacheManager;


    @Bean
    public StateMachine<ReviewStatus, ReviewAction, ReviewApplyVo> sectionStateMachine() {
        StateMachineBuilder<ReviewStatus, ReviewAction, ReviewApplyVo> builder = StateMachineBuilderFactory.create();

        // request section review for new review
        builder.externalTransitions().fromAmong(ReviewStatus.NONE, ReviewStatus.FINISHED).to(ReviewStatus.APPLIED)
                .on(ReviewAction.REQUEST).when(requestNoneSectionCon())
                .perform(doNoneSectionRequest());

        // request section review for rejected review
        builder.externalTransition().from(ReviewStatus.REJECTED).to(ReviewStatus.APPLIED)
                .on(ReviewAction.REQUEST).when(requestRejectSectionCon())
                .perform(doRequestRejectSection());

        // pass section review
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.FINISHED)
                .on(ReviewAction.PASS).when(reviewSectionCon())
                .perform(doPassSection());
        // reject section review
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.REJECTED)
                .on(ReviewAction.REJECT).when(reviewSectionCon())
                .perform(doRejectSection());
        return builder.build(StateMachineConstant.SECTION_STATE_MACHINE_ID);
    }

    private Condition<ReviewApplyVo> requestNoneSectionCon() {
        return (ctx) -> true;
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doNoneSectionRequest() {
        return (from, to, action, ctx) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                stateMachineService.requestNoneReview(ctx);

                LambdaUpdateWrapper<EduSection> wrapper = Wrappers.lambdaUpdate(EduSection.class);
                wrapper.eq(EduSection::getId,ctx.getId())
                        .set(EduSection::getReview, ReviewStatus.APPLIED);
                sectionMapper.update(null, wrapper);
            }
        });
    }


    @PreAuthorize("hasAuthority('section.review.reject')")
    private Condition<ReviewApplyVo> requestRejectSectionCon() {
        return (ctx) -> true;
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doRequestRejectSection() {
        return (from, to, action, ctx) ->
                transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                    @Override
                    protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                        stateMachineService.requestRejectedReview(ctx);
                        LambdaUpdateWrapper<EduSection> wrapper = Wrappers.lambdaUpdate(EduSection.class);
                        wrapper.eq(EduSection::getId, ctx.getId()).eq(EduSection::getReview,ctx.getFrom())
                                .set(EduSection::getReview, ctx.getTo());
                        sectionMapper.update(null, wrapper);

                    }
                });
//                sectionService.requestRejectedSectionReview(ctx);
    }

    private Condition<ReviewApplyVo> reviewSectionCon() {
        return (ctx) -> true;
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doPassSection() {
        return (from, to, action, ctx) -> {
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {

                    stateMachineService.doAlterReviewByCtx(ctx);
                    Long sectionId =ctx.getId();
                    // remove section if marked isRemoveAfterReview
                    EduSection section = sectionMapper.selectById(sectionId);
                    if (section.getIsRemoveAfterReview()) {
                        sectionMapper.deleteById(sectionId);
                        markdownMapper.deleteById(sectionId);
                        publishedMapper.deleteById(sectionId);
                        publishedMdMapper.deleteById(sectionId);
                    } else {
                        // save or update published section.
                        EduSectionMarkdown sectionMarkdown = markdownMapper.selectById(sectionId);
                        EduSectionPublished published = publishedMapper.selectById(sectionId);
                        EduSectionPublishedMd publishedMd = publishedMdMapper.selectById(sectionId);
                        section.setReview(ctx.getTo());
                        sectionMapper.updateById(section);
                        if (ObjectUtils.isEmpty(published)) {
                            published = new EduSectionPublished();
                            publishedMd = new EduSectionPublishedMd();
                        }
                        BeanUtils.copyProperties(section, published);
//                        published.setViewCount(null);
                        BeanUtils.copyProperties(sectionMarkdown, publishedMd);


                        if(published.getId()==null){
                            publishedMapper.insert(published);
                            publishedMdMapper.insert(publishedMd);
                        }else {
                            publishedMapper.updateById(published);
                            publishedMdMapper.updateById(publishedMd);

                        }
                        CacheUtils.evict(cacheManager,FrontCacheConst.SECTION_NAME,sectionId.toString());
                        CacheUtils.evict(cacheManager,FrontCacheConst.COURSE_NAME,
                                CacheUtils.getPrefix(FrontCacheConst.COURSE_TOC_KEY_PREFIX)+ section.getCourseId(),
                                CacheUtils.getPrefix(FrontCacheConst.COURSE_SYLLABUS_KEY_PREFIX)+ section.getCourseId()
                                );

                    }
                }
            });

//            sectionService.passSection(ctx);
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doRejectSection() {
        return (from, to, action, ctx) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {

                stateMachineService.doAlterReviewByCtx(ctx);
                // set review status to rejected and  isRemoveAfterReview to false,
                LambdaUpdateWrapper<EduSection> wrapper = Wrappers.lambdaUpdate(EduSection.class);
                wrapper.eq(EduSection::getId,ctx.getId())
                        .eq(EduSection::getReview, ReviewStatus.APPLIED)
                        .set(EduSection::getIsRemoveAfterReview, false)
                        .set(EduSection::getReview, ReviewStatus.REJECTED);
                sectionMapper.update(null, wrapper);

            }
        });
//        sectionService.rejectSection(ctx);
    }
}
