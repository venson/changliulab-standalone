package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.pojo.EduReviewMsg;
import com.venson.changliulabstandalone.entity.dto.ReviewDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewType;
import com.venson.changliulabstandalone.mapper.EduReviewMapper;
import com.venson.changliulabstandalone.service.admin.EduReviewMsgService;
import com.venson.changliulabstandalone.service.admin.EduReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-07-16
 */
@Service
@Slf4j
public class EduReviewServiceImp extends ServiceImpl<EduReviewMapper, EduReview> implements EduReviewService {


    @Autowired
    private EduReviewMsgService reviewMsgService;

    @Override
    public List<EduReview> getReviewByMethodologyId(Long id) {
        LambdaQueryWrapper<EduReview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduReview::getRefType, ReviewType.METHODOLOGY)
                .eq(EduReview::getRefId, id)
                .orderByAsc(EduReview::getId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<ReviewDTO> getReviewListByRefId(Long id, ReviewType type) {
        LambdaQueryWrapper<EduReview> reviewWrapper = Wrappers.lambdaQuery(EduReview.class);
        reviewWrapper.eq(EduReview::getRefId, id)
                .eq(EduReview::getRefType, type)
                .orderByDesc(EduReview::getId);
        List<EduReview> reviewList = baseMapper.selectList(reviewWrapper);
        LambdaQueryWrapper<EduReviewMsg> reviewMsgWrapper = Wrappers.lambdaQuery(EduReviewMsg.class);
        reviewMsgWrapper.eq(EduReviewMsg::getRefId, id)
                .eq(EduReviewMsg::getRefType, type)
                .orderByDesc(EduReviewMsg::getId);
        List<EduReviewMsg> msgList = reviewMsgService.list(reviewMsgWrapper);
        HashMap<Long, List<EduReviewMsg>> msgMap = new HashMap<>();
        msgList.forEach(msg->{
            Long reviewId =  msg.getReviewId();
            if(msgMap.containsKey(reviewId)){
                List<EduReviewMsg> list = msgMap.get(reviewId);
                list.add(msg);
            }else{
                LinkedList<EduReviewMsg> list = new LinkedList<>();
                list.add(msg);
                msgMap.put(reviewId,list);
            }
        });
        LinkedList<ReviewDTO> reviewDTOs = new LinkedList<>();
        reviewList.forEach(review ->{
            ReviewDTO temp = new ReviewDTO();
            BeanUtils.copyProperties(review,temp);
            temp.setMessages(msgMap.get(temp.getId()));
            reviewDTOs.add(temp);
        });

        return reviewDTOs;
    }

    @Override
    public List<EduReview> getReviewByResearchId(Long id) {
        LambdaQueryWrapper<EduReview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduReview::getRefType, ReviewType.RESEARCH)
                .eq(EduReview::getRefId, id)
                .orderByAsc(EduReview::getId);
        return baseMapper.selectList(wrapper);
    }

}
