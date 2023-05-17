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
import com.venson.changliulabstandalone.service.StateMachineService;
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
public class CourseStateMachineImpl {

    private final StateMachineService stateMachineService;

    private final EduCourseDescriptionMapper courseDescMapper;
    private final EduCoursePublishedMapper coursePublishedMapper;
    private final EduCourseDescriptionPublishedMapper courseDescPublishedMapper;

    private final TransactionTemplate transactionTemplate;

    private final EduChapterMapper chapterMapper;
    private final EduCourseMapper courseMapper;

    private final CacheManager cacheManager;

    private final StateMachine<ReviewStatus, ReviewAction, ReviewApplyVo> chapterStateMachine;


    @Bean
    public StateMachine<ReviewStatus, ReviewAction, ReviewApplyVo> courseStateMachine(){
        StateMachineBuilder<ReviewStatus,ReviewAction,ReviewApplyVo> builder = StateMachineBuilderFactory.create();
        // for Course
        builder.externalTransitions().fromAmong(ReviewStatus.NONE,ReviewStatus.FINISHED).to(ReviewStatus.APPLIED)
                .on(ReviewAction.REQUEST).when(requestNoneCourseCon())
                .perform(doRequestNoneCourse());
        builder.externalTransition().from(ReviewStatus.REJECTED).to(ReviewStatus.APPLIED)
                .on(ReviewAction.REQUEST).when(requestRejectedCourseCon())
                .perform(doRequestRejectedCourse());
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.FINISHED)
                .on(ReviewAction.PASS).when(passCourseCon())
                .perform(doPassCourse());
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.REJECTED)
                .on(ReviewAction.REJECT).when(rejectCourseCon())
                .perform(doRejectCourse());



        // For entire course
        builder.externalTransition().from(ReviewStatus.NONE).to(ReviewStatus.APPLIED)
                .on(ReviewAction.REQUEST_ENTIRE).when(requestEntireCourseCon())
                .perform(doRequestEntireCourse());
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.FINISHED)
                .on(ReviewAction.PASS_ENTIRE).when(passEntireCourseCon())
                .perform(doPassEntireCourse());
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.REJECTED)
                .on(ReviewAction.REJECT_ENTIRE).when(rejectEntireCourseCon())
                .perform(doRejectEntireCourse());
        return builder.build(StateMachineConstant.COURSE_STATE_MACHINE_ID);
    }
    // request for course info
    @PreAuthorize("hasAuthority('course.review.request')")
    private Condition<ReviewApplyVo> requestNoneCourseCon() {
        return (ctx) -> {
            EduCourse course= courseMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(course) && course.getReview() == ctx.getFrom();
        };
    }
    private Action<ReviewStatus, ReviewAction,ReviewApplyVo> doRequestNoneCourse(){
        return (from, to, event, context)-> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                stateMachineService.requestNoneReview(context);
                updateCourseReviewStatusByCourseId(context.getId(), ReviewStatus.APPLIED);
            }
        });
    }

    private void updateCourseReviewStatusByCourseId(Long id, ReviewStatus status) {
        LambdaUpdateWrapper<EduCourse> wrapper = Wrappers.lambdaUpdate(EduCourse.class);
        wrapper.eq(EduCourse::getId, id).set(EduCourse::getReview, status);
        courseMapper.update(null, wrapper);
    }

    @PreAuthorize("hasAuthority('course.review.request')")
    private Condition<ReviewApplyVo> requestRejectedCourseCon() {
        return (ctx) -> {
            EduCourse course= courseMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(course) && course.getReview() == ctx.getFrom();
        };
    }
    private Action<ReviewStatus, ReviewAction,ReviewApplyVo> doRequestRejectedCourse(){
        return (from, to, event, context)-> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                stateMachineService.requestRejectedReview(context);
                updateCourseReviewStatusByCourseId(context.getId(), ReviewStatus.APPLIED);
            }
        });

    }

    /**
     * Condition check for pass course action;
     * @return state machine condition
     */
    private Condition<ReviewApplyVo> passCourseCon() {
        return (ctx) -> {
            EduCourse course= courseMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(course) && course.getReview() == ctx.getFrom();
        };
    }
    private Action<ReviewStatus, ReviewAction,ReviewApplyVo> doPassCourse(){
        return (from, to, event, context)-> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {

                stateMachineService.doAlterReviewByCtx(context);
                Long id = context.getId();
                EduCourse  course= courseMapper.selectById(id);
                if (course.getIsRemoveAfterReview()) {
                    courseMapper.deleteById(id);
                    courseDescMapper.deleteById(id);
                    coursePublishedMapper.deleteById(id);
                    courseDescPublishedMapper.deleteById(id);
                } else {
                    course.setReview(context.getTo());
                    courseMapper.updateById(course);
                    EduCourseDescription courseDescription=courseDescMapper.selectById(id);
                    EduCoursePublished published =coursePublishedMapper.selectById(id);
                    EduCourseDescriptionPublished publishedDesc =courseDescPublishedMapper.selectById(id);
                    if (ObjectUtils.isEmpty(published)) {
                        published = new EduCoursePublished();
                        publishedDesc = new EduCourseDescriptionPublished();
                        BeanUtils.copyProperties(course, published);
                        BeanUtils.copyProperties(courseDescription, publishedDesc);
                        coursePublishedMapper.insert(published);
                        courseDescPublishedMapper.insert(publishedDesc);
                    }else {
                        BeanUtils.copyProperties(course, published);
                        BeanUtils.copyProperties(courseDescription, publishedDesc);
                        coursePublishedMapper.updateById(published);
                        courseDescPublishedMapper.updateById(publishedDesc);
                    }
//                    CacheUtils.evict(cacheManager, FrontCacheConst.COURSE_NAME,
//                            CacheUtils.getPrefix(FrontCacheConst.COURSE_TOC_KEY_PREFIX)+ id,
//                            CacheUtils.getPrefix(FrontCacheConst.COURSE_SYLLABUS_KEY_PREFIX)+id);
                    CacheUtils.evict(cacheManager,FrontCacheConst.COURSE_NAME, CacheUtils.getPrefix(FrontCacheConst.COURSE_DESC_KEY_PREFIX) +   id);

                }
            }
        });
    }
    @PreAuthorize("hasAuthority('course.review.reject')")
    private Condition<ReviewApplyVo> rejectCourseCon() {
        return (ctx) -> {
            EduCourse course= courseMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(course) && course.getReview() == ctx.getFrom();
        };
    }
    private Action<ReviewStatus, ReviewAction,ReviewApplyVo> doRejectCourse(){
        return (from, to, event, context)-> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                stateMachineService.doAlterReviewByCtx(context);
                updateCourseReviewStatusByCourseId(context.getId(), ReviewStatus.REJECTED);
            }
        });
    }







    @PreAuthorize("hasAuthority('course.review.request')")
    private Condition<ReviewApplyVo> requestEntireCourseCon() {
        return (ctx) -> {
            EduCourse course= courseMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(course) && course.getReview() == ctx.getFrom();
        };
    }
    private Action<ReviewStatus, ReviewAction,ReviewApplyVo> doRequestEntireCourse(){
        return (from, to, event, context)-> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                doEntireCourseReview(context);
                context.setAction(ReviewAction.REQUEST);
                context.setType(ReviewType.COURSE);
                context.setParentType(null);
                context.setParentId(null);
                courseStateMachine().fireEvent(context.getFrom(), context.getAction(), context);
            }
        });
    }

    private void doEntireCourseReview(ReviewApplyVo context) {

        Long courseId=context.getId();
        LambdaQueryWrapper<EduChapter> wrapper = Wrappers.lambdaQuery(EduChapter.class);
        wrapper.eq(EduChapter::getCourseId, courseId)
                .select(EduChapter::getId,EduChapter::getReview);
        List<EduChapter> chapters= chapterMapper.selectList(wrapper);
        context.setType(ReviewType.CHAPTER);
        context.setParentType(ReviewType.COURSE);
        context.setParentId(courseId);
        chapters.forEach(chapter->{
            context.setId(chapter.getId());
            context.setFrom(chapter.getReview());
            chapterStateMachine.fireEvent(context.getFrom(),context.getAction(),context);

        });
    }

    @PreAuthorize("hasAuthority('course.review.pass')")
    private Condition<ReviewApplyVo> passEntireCourseCon() {
        return (ctx) -> {
            EduCourse course= courseMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(course) && course.getReview() == ctx.getFrom();
        };
    }
    private Action<ReviewStatus, ReviewAction,ReviewApplyVo> doPassEntireCourse(){
        return (from, to, event, context)-> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                doEntireCourseReview(context);
                context.setAction(ReviewAction.PASS);
                context.setType(ReviewType.COURSE);
                context.setParentType(null);
                context.setParentId(null);
                courseStateMachine().fireEvent(context.getFrom(), context.getAction(), context);
            }
        });
    }
    @PreAuthorize("hasAuthority('course.review.reject')")
    private Condition<ReviewApplyVo> rejectEntireCourseCon() {
        return (ctx) -> {
            EduCourse course= courseMapper.selectById(ctx.getId());
            return !ObjectUtils.isEmpty(course) && course.getReview() == ctx.getFrom();
        };
    }
    private Action<ReviewStatus, ReviewAction,ReviewApplyVo> doRejectEntireCourse(){
        return (from, to, event, context)-> transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NonNull TransactionStatus status) {
                doEntireCourseReview(context);
                context.setAction(ReviewAction.REJECT);
                context.setType(ReviewType.COURSE);
                context.setParentType(null);
                context.setParentId(null);
                courseStateMachine().fireEvent(context.getFrom(), context.getAction(), context);
            }
        });

    }
}
