package com.venson.changliulabstandalone.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
import com.venson.changliulabstandalone.entity.enums.ReviewAction;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.enums.ReviewType;
import com.venson.changliulabstandalone.entity.pojo.*;
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

import java.util.List;


@Configuration
@RequiredArgsConstructor
public class ChapterStateMachineImpl {

    private final StateMachineService stateMachineService;
    private final EduSectionMapper sectionMapper;
    private final EduChapterDescriptionMapper chapterDescriptionMapper;
    private final TransactionTemplate transactionTemplate;
    private final EduChapterMapper chapterMapper;
    private final CacheManager cacheManager;
    private final EduChapterPublishedDescMapper chapterPublishedDescMapper;
    private final EduChapterPublishedMapper chapterPublishedMapper;
    private final StateMachine<ReviewStatus, ReviewAction, ReviewApplyVo> sectionStateMachine;


    @Bean
    public StateMachine<ReviewStatus, ReviewAction, ReviewApplyVo> chapterStateMachine() {
        StateMachineBuilder<ReviewStatus, ReviewAction, ReviewApplyVo> builder = StateMachineBuilderFactory.create();
        builder.externalTransitions().fromAmong(ReviewStatus.NONE, ReviewStatus.FINISHED).to(ReviewStatus.APPLIED).on(ReviewAction.REQUEST).when(requestNoneChapterCon()).perform(doRequestNoneChapter());
        builder.externalTransition().from(ReviewStatus.REJECTED).to(ReviewStatus.APPLIED).on(ReviewAction.REQUEST).when(requestRejectedChapterCon()).perform(doRequestRejectedChapter());
        // Pass chapter
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.FINISHED).on(ReviewAction.PASS).when(passChapterCon()).perform(doPassChapter());
        // Reject chapter
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.REJECTED).on(ReviewAction.REJECT).when(rejectChapterCon()).perform(doRejectChapter());

        // request entire chapter
        builder.externalTransitions().fromAmong(ReviewStatus.NONE, ReviewStatus.APPLIED, ReviewStatus.FINISHED, ReviewStatus.REJECTED).to(ReviewStatus.APPLIED).on(ReviewAction.REQUEST_ENTIRE).when(requestEntireChapterCon()).perform(doRequestEntireChapter());
        // Pass entire chapter
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.FINISHED).on(ReviewAction.PASS_ENTIRE).when(passEntireChapterCon()).perform(doPassEntireChapter());
        // Reject entire chapter
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.REJECTED).on(ReviewAction.REJECT_ENTIRE).when(rejectEntireChapterCon()).perform(doRejectEntireChapter());
        return builder.build(StateMachineConstant.CHAPTER_STATE_MACHINE_ID);
    }

    @PreAuthorize("hasAuthority('course.review.request')")
    private Condition<ReviewApplyVo> requestNoneChapterCon() {
        return (ctx) -> {
            EduChapter chapter = chapterMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(chapter) && chapter.getReview() == ctx.getFrom();
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doRequestNoneChapter() {
        return (from, to, event,ctx) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                stateMachineService.requestNoneReview(ctx);
                updateChapterReviewStatusByChapterId(ctx.getId(), ReviewStatus.APPLIED);
            }
        });
    }


    @PreAuthorize("hasAuthority('course.review.request')")
    private Condition<ReviewApplyVo> requestRejectedChapterCon() {
        return (ctx) -> {
            EduChapter chapter = chapterMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(chapter) && chapter.getReview() == ctx.getFrom();
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doRequestRejectedChapter() {
        return (from, to, event, context) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                stateMachineService.requestRejectedReview(context);
                updateChapterReviewStatusByChapterId(context.getId(), ReviewStatus.APPLIED);
            }
        });
    }


    @PreAuthorize("hasAuthority('course.review.pass')")
    private Condition<ReviewApplyVo> passChapterCon() {
        return (ctx) -> {
            EduChapter chapter = chapterMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(chapter) && chapter.getReview() == ctx.getFrom();
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doPassChapter() {
        return (from, to, event, context) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {

                stateMachineService.doAlterReviewByCtx(context);
                Long id = context.getId();
                EduChapter chapter = chapterMapper.selectById(id);
                if (chapter.getIsRemoveAfterReview()) {
                    chapterMapper.deleteById(id);
                    chapterDescriptionMapper.deleteById(id);
                    chapterPublishedMapper.deleteById(id);
                    chapterPublishedDescMapper.deleteById(id);
                } else {
                    EduChapterDescription sectionMarkdown =chapterDescriptionMapper.selectById(id);
                    EduChapterPublished published =chapterPublishedMapper.selectById(id);
                    EduChapterPublishedDesc publishedDesc = chapterPublishedDescMapper.selectById(id);
                    chapter.setReview(context.getTo());
                    chapterMapper.updateById(chapter);
                    if (ObjectUtils.isEmpty(published)) {
                        published = new EduChapterPublished();
                        publishedDesc = new EduChapterPublishedDesc();
                        BeanUtils.copyProperties(chapter, published);
                        BeanUtils.copyProperties(sectionMarkdown, publishedDesc);
                        chapterPublishedMapper.insert(published);
                        chapterPublishedDescMapper.insert(publishedDesc);
                    }else {
                        BeanUtils.copyProperties(chapter, published);
                        BeanUtils.copyProperties(sectionMarkdown, publishedDesc);
                        chapterPublishedMapper.updateById(published);
                        chapterPublishedDescMapper.updateById(publishedDesc);
                    }
                    CacheUtils.evict(cacheManager,FrontCacheConst.COURSE_NAME,
                            CacheUtils.getPrefix(FrontCacheConst.COURSE_TOC_KEY_PREFIX)+ chapter.getCourseId(),
                            CacheUtils.getPrefix(FrontCacheConst.COURSE_SYLLABUS_KEY_PREFIX)+chapter.getCourseId()
                    );

                }
            }
        });
    }

    @PreAuthorize("hasAuthority('course.review.reject')")
    private Condition<ReviewApplyVo> rejectChapterCon() {
        return (ctx) -> {
            EduChapter chapter = chapterMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(chapter) && chapter.getReview() == ctx.getFrom();
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doRejectChapter() {
        return (from, to, event, context) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                stateMachineService.doAlterReviewByCtx(context);
                updateChapterReviewStatusByChapterId(context.getId(), ReviewStatus.REJECTED);
            }
        });
    }

    @PreAuthorize("hasAuthority('course.review.request')")
    private Condition<ReviewApplyVo> requestEntireChapterCon() {
        return (ctx) -> {
            EduChapter chapter = chapterMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(chapter) && chapter.getReview() == ctx.getFrom();
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doRequestEntireChapter() {
        return (from, to, event, context) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                context.setAction(ReviewAction.REQUEST);
                doEntireChapterReview(context,null,ReviewStatus.APPLIED);
            }
        });
    }

    /**
     * pass review for  entire chapter when condition
     *
     * @return Condition
     */
    @PreAuthorize("hasAuthority('chapter.review.pass')")
    private Condition<ReviewApplyVo> passEntireChapterCon() {
        return (ctx) -> {
            EduChapter chapter = chapterMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(chapter) && chapter.getReview() == ctx.getFrom();
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doPassEntireChapter() {
        return (from, to, event, context) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                context.setAction(ReviewAction.PASS);
                doEntireChapterReview(context,ReviewStatus.APPLIED);
            }

        });
    }

    private void doEntireChapterReview(ReviewApplyVo context,ReviewStatus eqFilter ,ReviewStatus ...neFilter) {
        Long chapterId =context.getId();
        chapterStateMachine().fireEvent(context.getFrom(), context.getAction(), context);
        LambdaQueryWrapper<EduSection> wrapper = Wrappers.lambdaQuery(EduSection.class);
        wrapper.eq(EduSection::getChapterId,chapterId)
                .eq(EduSection::getIsModified,true)
                .select(EduSection::getId,EduSection::getReview);
        if(neFilter!=null){
            for (ReviewStatus reviewStatus : neFilter) {
                wrapper.ne(EduSection::getReview, reviewStatus);
            }
        }
        if(eqFilter!=null){
            wrapper.eq(EduSection::getReview, eqFilter);
        }
        List<EduSection> sections = sectionMapper.selectList(wrapper);
        context.setType(ReviewType.SECTION);
        context.setParentType(ReviewType.CHAPTER);
        context.setParentId(chapterId);
        sections.forEach(section ->{
            context.setId(section.getId());
            context.setFrom(section.getReview());
            sectionStateMachine.fireEvent(context.getFrom(),context.getAction(),context);

        });
    }

    @PreAuthorize("hasAuthority('course.review.reject')")
    private Condition<ReviewApplyVo> rejectEntireChapterCon() {
        return (ctx) -> {
            EduChapter chapter = chapterMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(chapter) && chapter.getReview() == ctx.getFrom();
        };
    }

    private Action<ReviewStatus, ReviewAction, ReviewApplyVo> doRejectEntireChapter() {
        return (from, to, event, context) -> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                context.setAction(ReviewAction.REJECT);
                doEntireChapterReview(context,ReviewStatus.APPLIED);

            }
        });
    }


    private void updateChapterReviewStatusByChapterId(Long id, ReviewStatus status) {
        LambdaUpdateWrapper<EduChapter> wrapper = Wrappers.lambdaUpdate(EduChapter.class);
        wrapper.eq(EduChapter::getId, id).set(EduChapter::getReview, status);
        chapterMapper.update(null, wrapper);

    }
}
