package com.venson.changliulab.statemachine;

import com.alibaba.cola.statemachine.Action;
import com.alibaba.cola.statemachine.Condition;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;

public interface ReviewStateMachine {

    Condition<ReviewSMContext> checkRequestNew();
    Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestNew();
    Condition<ReviewSMContext> checkRequestRejected();
    Action<ReviewStatus, ReviewAction, ReviewSMContext> doRequestRejected();
    Condition<ReviewSMContext> checkPass();
    Action<ReviewStatus, ReviewAction, ReviewSMContext> doPass();
    Condition<ReviewSMContext> checkReject();
    Action<ReviewStatus, ReviewAction, ReviewSMContext> doReject();
}
