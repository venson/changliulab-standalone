package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.entity.pojo.EduReviewMsg;
import com.venson.changliulabstandalone.mapper.EduReviewMsgMapper;
import com.venson.changliulabstandalone.service.admin.EduReviewMsgService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author venson
 * @since 2023-01-25
 */
@Service
public class EduReviewMsgServiceImp extends ServiceImpl<EduReviewMsgMapper, EduReviewMsg> implements EduReviewMsgService {

//    @Override
//    public EduReviewMsg getUnfinishedMsgByReviewId(Long id) {
//        LambdaQueryWrapper<EduReviewMsg> wrapper = Wrappers.lambdaQuery(EduReviewMsg.class);
//        wrapper.eq(EduReviewMsg::getReviewId, id).isNull(EduReviewMsg::getReviewAction);
//        return baseMapper.selectOne(wrapper);
//    }
}
