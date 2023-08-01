package com.venson.changliulab.statemachine;

import com.alibaba.cola.statemachine.StateMachine;
import com.venson.changliulab.entity.enums.ReviewType;
import com.venson.changliulab.entity.vo.admin.ReviewVo;

public interface StateMachineFactory<S,E,C> {
    StateMachine<S,E,C> getStateMachine(ReviewType type);
    void dispatch(ReviewVo reviewVo);
}
