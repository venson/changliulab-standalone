package com.venson.changliulabstandalone.service.smimpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.pojo.EduReviewMsg;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.enums.ReviewType;
import com.venson.changliulabstandalone.mapper.EduReviewMapper;
import com.venson.changliulabstandalone.mapper.EduReviewMsgMapper;
import com.venson.changliulabstandalone.service.StateMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


@Service
public class StateMachineServiceImpl implements StateMachineService {
    @Autowired
    EduReviewMapper reviewMapper;
    @Autowired
    EduReviewMsgMapper reviewMsgMapper;


    @Override
    public void doAlterReviewByCtx(ReviewApplyVo ctx) {
        EduReview review = getUnfinishedReviewByRefIdType(ctx.getId(), ctx.getType());
        review.setStatus(ctx.getTo());

        reviewMapper.updateById(review);

        LambdaQueryWrapper<EduReviewMsg> wrapper = Wrappers.lambdaQuery(EduReviewMsg.class);
        wrapper.eq(EduReviewMsg::getReviewId, review.getId())
                .isNull(EduReviewMsg::getReviewAction);

        EduReviewMsg msg =reviewMsgMapper.selectOne(wrapper);

        ctx.copyReviewMsgToEduReviewMsg(msg);
        reviewMsgMapper.updateById(msg);

    }
    @Override
    public EduReview getUnfinishedReviewByRefIdType(Long refId, ReviewType refType) {
        LambdaQueryWrapper<EduReview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduReview::getRefType, refType)
                .eq(EduReview::getRefId, refId)
                .ne(EduReview::getStatus, ReviewStatus.FINISHED);
        return reviewMapper.selectOne(wrapper);
    }


    @Override
    @Transactional
    public void requestRejectedReview(ReviewApplyVo ctx) {
        LambdaQueryWrapper<EduReview> wrapper = Wrappers.lambdaQuery(EduReview.class);
        wrapper.eq(EduReview::getRefId, ctx.getId())
                        .eq(EduReview::getRefType, ctx.getType())
                .eq(EduReview::getStatus,ReviewStatus.REJECTED);

        EduReview review = reviewMapper.selectOne(wrapper);
        Assert.notNull(review,"Content not found");
        review.setStatus(ReviewStatus.APPLIED);
        reviewMapper.updateById(review);

        EduReviewMsg msg = new EduReviewMsg();
        ctx.copyRequestMsgToEduReviewMsg(msg);
        msg.setReviewId(review.getId());
        reviewMsgMapper.insert(msg);
    }
    @Override
    public void requestNoneReview(ReviewApplyVo ctx) {
        EduReview eduReview = new EduReview();
        eduReview.setRefId(ctx.getId());
        eduReview.setRefType(ctx.getType());
        eduReview.setStatus(ctx.getTo());
        eduReview.setRefParentId(ctx.getParentId());
        eduReview.setRefParentType(ctx.getParentType());
        reviewMapper.insert(eduReview);

        EduReviewMsg msg = new EduReviewMsg();
        ctx.copyRequestMsgToEduReviewMsg(msg);
        msg.setReviewId(eduReview.getId());
        reviewMsgMapper.insert(msg);
    }
}
