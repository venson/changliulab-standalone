package com.venson.changliulab.service;

import com.venson.changliulab.entity.vo.admin.ReviewSMContext;


public interface StateMachineService {
    void requestReviews(ReviewSMContext ctx);

    void requestRejectedReview(ReviewSMContext context);

    void passReviews(ReviewSMContext context);

    void rejectReviews(ReviewSMContext context);
}
