package com.venson.changliulab.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduMethodologyPublished;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
public interface EduMethodologyPublishedService extends IService<EduMethodologyPublished> {

    ReviewAble getReviewById(Long refId);
}
