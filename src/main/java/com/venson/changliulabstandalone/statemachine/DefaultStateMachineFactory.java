package com.venson.changliulabstandalone.statemachine;

import com.alibaba.cola.statemachine.StateMachine;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
import com.venson.changliulabstandalone.entity.enums.ReviewAction;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.enums.ReviewType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DefaultStateMachineFactory implements StateMachineFactory<ReviewStatus, ReviewAction, ReviewApplyVo>{

    private final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> courseStateMachine;
    private final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> researchStateMachine;
    private final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> methodologyStateMachine;
    private  final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> activityStateMachine;
    private final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> chapterStateMachine;

    private final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> sectionStateMachine;
    private final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> reportStateMachine;
    @Override
    public StateMachine<ReviewStatus, ReviewAction, ReviewApplyVo> getStateMachine(ReviewType type) {
        switch (type) {
            case CHAPTER -> {
                return chapterStateMachine;
            }
            case COURSE -> {
                return courseStateMachine;
            }
            case SECTION -> {
                return sectionStateMachine;
            }
            case REPORT -> {
                return reportStateMachine;
            }
            case ACTIVITY -> {
                return activityStateMachine;
            }
            case RESEARCH -> {
                return researchStateMachine;
            }
            case METHODOLOGY -> {
                return methodologyStateMachine;
            }
            default -> {
                return null;
            }
        }
    }
}
