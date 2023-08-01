package com.venson.changliulab.service.admin;

import com.venson.changliulab.entity.pojo.EduReviewMsg;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2023-01-25
 */
public interface EduReviewMsgService extends IService<EduReviewMsg> {
    List<EduReviewMsg> getReviewMessageById(Long id);

//    EduReviewMsg getUnfinishedMsgByReviewId(Long id);
}
