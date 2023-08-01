package com.venson.changliulab.statemachine;

import com.alibaba.cola.statemachine.StateMachine;
import com.alibaba.cola.statemachine.builder.StateMachineBuilder;
import com.alibaba.cola.statemachine.builder.StateMachineBuilderFactory;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.statemachine.StateMachineConstant;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StateMachineBean {

    private final ReviewStateMachine chapterStateMachineImpl;
    private final ReviewStateMachine sectionStateMachineImpl;
    private final ReviewStateMachine methodologyStateMachineImpl;
    private final ReviewStateMachine activityStateMachineImpl;
    private final ReviewStateMachine reportStateMachineImpl;
    private final ReviewStateMachine courseStateMachineImpl;
    private final ReviewStateMachine researchStateMachineImpl;

    @Bean
    public StateMachine<ReviewStatus, ReviewAction, ReviewSMContext> chapterStateMachine() {
        return createStateMachine(chapterStateMachineImpl, StateMachineConstant.CHAPTER_STATE_MACHINE_ID);
    }
    @Bean
    public StateMachine<ReviewStatus, ReviewAction, ReviewSMContext> sectionStateMachine() {
        return createStateMachine(sectionStateMachineImpl, StateMachineConstant.SECTION_STATE_MACHINE_ID);
    }

    @Bean
    public StateMachine<ReviewStatus, ReviewAction, ReviewSMContext> methodologyStateMachine() {
        return createStateMachine(methodologyStateMachineImpl, StateMachineConstant.METHODOLOGY_STATE_MACHINE_ID);
    }
    @Bean
    public StateMachine<ReviewStatus, ReviewAction, ReviewSMContext> activityStateMachine(){
        return createStateMachine(activityStateMachineImpl, StateMachineConstant.ACTIVITY_STATE_MACHINE_ID);
    }

    @Bean
    public StateMachine<ReviewStatus, ReviewAction, ReviewSMContext> reportStateMachine(){
        return createStateMachine(reportStateMachineImpl, StateMachineConstant.REPORT_STATE_MACHINE_ID);
    }
    @Bean
    public StateMachine<ReviewStatus, ReviewAction, ReviewSMContext> courseStateMachine(){
        return createStateMachine(courseStateMachineImpl, StateMachineConstant.COURSE_STATE_MACHINE_ID);
    }
    @Bean
    public StateMachine<ReviewStatus, ReviewAction, ReviewSMContext> researchStateMachine(){
        return createStateMachine(researchStateMachineImpl, StateMachineConstant.RESEARCH_STATE_MACHINE_ID);
    }

    public StateMachine<ReviewStatus, ReviewAction, ReviewSMContext> createStateMachine(ReviewStateMachine reviewStateMachine, String id) {
        StateMachineBuilder<ReviewStatus, ReviewAction, ReviewSMContext> builder = StateMachineBuilderFactory.create();
        builder.externalTransitions().fromAmong(ReviewStatus.NONE, ReviewStatus.FINISHED)
                .to(ReviewStatus.APPLIED)
                .on(ReviewAction.REQUEST)
                .when(reviewStateMachine.checkRequestNew())
                .perform(reviewStateMachine.doRequestNew());
        builder.externalTransition().from(ReviewStatus.REJECTED).to(ReviewStatus.APPLIED).on(ReviewAction.REQUEST)
                .when(reviewStateMachine.checkRequestRejected()).perform(reviewStateMachine.doRequestRejected());
        // Pass chapter
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.FINISHED).on(ReviewAction.PASS)
                .when(reviewStateMachine.checkPass()).perform(reviewStateMachine.doPass());
        // Reject chapter
        builder.externalTransition().from(ReviewStatus.APPLIED).to(ReviewStatus.REJECTED).on(ReviewAction.REJECT)
                .when(reviewStateMachine.checkReject()).perform(reviewStateMachine.doReject());

        return builder.build(id);
    }
}
