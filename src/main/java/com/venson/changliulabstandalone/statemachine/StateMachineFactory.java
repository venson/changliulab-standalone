package com.venson.changliulabstandalone.statemachine;

import com.alibaba.cola.statemachine.StateMachine;
import com.venson.changliulabstandalone.entity.enums.ReviewType;

public interface StateMachineFactory<S,E,C> {
    StateMachine<S,E,C> getStateMachine(ReviewType type);
}
