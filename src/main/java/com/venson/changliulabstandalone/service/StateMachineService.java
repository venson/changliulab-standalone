package com.venson.changliulabstandalone.service;

import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
import com.venson.changliulabstandalone.entity.enums.ReviewType;


public interface StateMachineService {

    void requestNoneReview(ReviewApplyVo ctx);

    void doAlterReviewByCtx(ReviewApplyVo ctx);

    EduReview getUnfinishedReviewByRefIdType(Long refId, ReviewType refType);


    void requestRejectedReview(ReviewApplyVo ctx);
}
