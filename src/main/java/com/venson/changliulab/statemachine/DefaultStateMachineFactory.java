package com.venson.changliulab.statemachine;

import com.alibaba.cola.statemachine.StateMachine;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.enums.ReviewType;
import com.venson.changliulab.entity.vo.admin.ReviewItemVo;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import com.venson.changliulab.entity.vo.admin.ReviewVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class DefaultStateMachineFactory implements StateMachineFactory<ReviewStatus, ReviewAction, ReviewSMContext>{


    private final StateMachine<ReviewStatus, ReviewAction,ReviewSMContext> courseStateMachine;
    private final StateMachine<ReviewStatus, ReviewAction,ReviewSMContext> researchStateMachine;
    private final StateMachine<ReviewStatus, ReviewAction,ReviewSMContext> methodologyStateMachine;
    private  final StateMachine<ReviewStatus, ReviewAction,ReviewSMContext> activityStateMachine;
    private final StateMachine<ReviewStatus, ReviewAction,ReviewSMContext> chapterStateMachine;

    private final StateMachine<ReviewStatus, ReviewAction,ReviewSMContext> sectionStateMachine;
    private final StateMachine<ReviewStatus, ReviewAction,ReviewSMContext> reportStateMachine;


    @Override
    public StateMachine<ReviewStatus, ReviewAction, ReviewSMContext> getStateMachine(ReviewType type) {
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

    @Override
    public void dispatch(ReviewVo reviewVo) {
        Map<ReviewType, List<Long>> typeIdMap= reviewVo.reviews().stream().collect(Collectors.groupingBy(ReviewItemVo::type, Collectors.mapping(ReviewItemVo::id, Collectors.toList())));
        typeIdMap.forEach((key, value) -> getStateMachine(key).fireEvent(reviewVo.from(), reviewVo.action(), new ReviewSMContext(value,key ,reviewVo.message(),reviewVo.from())));
    }
}
