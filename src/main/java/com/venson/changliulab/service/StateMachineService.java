package com.venson.changliulab.service;

import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.entity.dto.ReviewApplyVo;
import com.venson.changliulab.entity.enums.ReviewType;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;


public interface StateMachineService {
    void requestReviews(ReviewSMContext ctx);

    void requestRejectedReview(ReviewSMContext context);

    void passReviews(ReviewSMContext context);

    void rejectReviews(ReviewSMContext context);
}
