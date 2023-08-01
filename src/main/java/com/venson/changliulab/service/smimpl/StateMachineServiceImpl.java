package com.venson.changliulab.service.smimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.enums.ReviewType;
import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.entity.pojo.EduReviewMsg;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import com.venson.changliulab.service.StateMachineService;
import com.venson.changliulab.service.admin.EduReviewMsgService;
import com.venson.changliulab.service.admin.EduReviewService;
import com.venson.changliulab.utils.ContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class StateMachineServiceImpl implements StateMachineService {


    private final EduReviewService reviewService;
    private final EduReviewMsgService reviewMsgService;





    @Override
    @Transactional
    public void requestReviews(ReviewSMContext ctx) {
//        String username = ContextUtils.getUserName();
        List<Long> ids = ctx.ids();
        if(ids == null || ids.size() == 0){
            return;
        }
        List<EduReview> eduReviews = ids.stream().map(id ->
                EduReview.builder().refId(id)
                        .refType(ctx.type())
                        .status(ReviewStatus.APPLIED)
                        .build()
        ).collect(Collectors.toList());
        reviewService.saveBatch(eduReviews);

        List<EduReviewMsg> eduReviewMessages = eduReviews.stream().map(review ->
                EduReviewMsg.builder()
                        .reviewId(review.getId())
                        .refId(review.getRefId())
                        .requestMsg(ctx.message())
                        .requestUsername(ContextUtils.getUserName())
                        .requestUserId(ContextUtils.getUserId())
                        .build()
        ).toList();

        reviewMsgService.saveBatch(eduReviewMessages);
    }

    @Override
    @Transactional
    public void requestRejectedReview(ReviewSMContext context) {
        List<Long> ids = context.ids();
        if(ids == null || ids.size() == 0){
            return;
        }
        LambdaQueryWrapper<EduReview> wrapper = Wrappers.lambdaQuery(EduReview.class);
        wrapper.in(EduReview::getRefId, ids)
                .eq(EduReview::getStatus,ReviewStatus.REJECTED);

        List<EduReview> reviews = reviewService.list(wrapper);
        Assert.notEmpty(reviews,"Content not found");
        reviews.forEach(review -> {
            review.setStatus(ReviewStatus.APPLIED);
//            reviewMapper.updateById(review);
        });
        reviewService.saveOrUpdateBatch(reviews);

        List<EduReviewMsg> eduReviewMessages = reviews.stream().map(review ->
                EduReviewMsg.builder()
                        .reviewId(review.getId())
                        .refId(review.getRefId())
                        .reviewAction(ReviewAction.PENDING)
                        .requestMsg(context.message())
                        .requestUsername(ContextUtils.getUserName())
                        .requestUserId(ContextUtils.getUserId())
                        .build()
        ).toList();

        reviewMsgService.saveBatch(eduReviewMessages);

    }

    /**
     * used for pass reviews, update date review relate tables
     * update edu_review table, change review status to finished
     * update edu_review_msg table, finish the review message
     * @param context review statemachine context
     */
    @Override
    @Transactional
    public void passReviews(ReviewSMContext context) {
        List<Long> ids = context.ids();
        if(ids == null || ids.size() == 0){
            log.error("reviews ids is empty");
            return;
        }
        LambdaUpdateWrapper<EduReview> wrapper = Wrappers.lambdaUpdate(EduReview.class);
        wrapper.in(EduReview::getRefId, ids)
                .eq(EduReview::getStatus,ReviewStatus.APPLIED)
                .set(EduReview::getStatus,ReviewStatus.FINISHED);


        reviewService.update(wrapper);

        LambdaUpdateWrapper<EduReviewMsg> msgWrapper = Wrappers.lambdaUpdate(EduReviewMsg.class);
        msgWrapper.in(EduReviewMsg::getRefId,ids)
                .eq(EduReviewMsg::getReviewAction, ReviewAction.PENDING)
                .set(EduReviewMsg::getReviewId,ContextUtils.getUserId())
                .set(EduReviewMsg::getReviewUsername,ContextUtils.getUserName())
                .set(EduReviewMsg::getReviewMsg, context.message())
                .set(EduReviewMsg::getReviewAction, ReviewAction.PASS);
        reviewMsgService.update(msgWrapper);
    }

    @Override
    public void rejectReviews(ReviewSMContext context) {
        List<Long> ids = context.ids();
        if(ids == null || ids.size() == 0){
            return;
        }
        LambdaUpdateWrapper<EduReview> wrapper = Wrappers.lambdaUpdate(EduReview.class);
        wrapper.in(EduReview::getRefId, ids)
                .eq(EduReview::getStatus,ReviewStatus.APPLIED)
                .set(EduReview::getStatus,ReviewStatus.REJECTED);
        reviewService.update(wrapper);

        LambdaUpdateWrapper<EduReviewMsg> msgWrapper = Wrappers.lambdaUpdate(EduReviewMsg.class);
        msgWrapper.in(EduReviewMsg::getRefId,ids)
                .eq(EduReviewMsg::getReviewAction, ReviewAction.PENDING)
                .set(EduReviewMsg::getReviewId,ContextUtils.getUserId())
                .set(EduReviewMsg::getReviewUsername,ContextUtils.getUserName())
                .set(EduReviewMsg::getReviewMsg, context.message())
                .set(EduReviewMsg::getReviewAction, ReviewAction.REJECT);
        reviewMsgService.update(msgWrapper);

    }

}
