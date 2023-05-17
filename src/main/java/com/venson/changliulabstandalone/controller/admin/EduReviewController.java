package com.venson.changliulabstandalone.controller.admin;

import com.alibaba.cola.statemachine.StateMachine;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.vo.admin.ListQueryParams;
import com.venson.changliulabstandalone.service.admin.EduReviewService;
import com.venson.changliulabstandalone.statemachine.StateMachineFactory;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
import com.venson.changliulabstandalone.entity.dto.ReviewDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewAction;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.enums.ReviewType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  the review controller for course,chapter and section
 * </p>
 *
 * @author venson
 * @since 2022-07-16
 */
@RestController
@RequestMapping("/eduservice/admin/edu-review")
@Slf4j
@RequiredArgsConstructor
public class EduReviewController {



    private final EduReviewService reviewService;

    private final StateMachineFactory<ReviewStatus, ReviewAction,ReviewApplyVo> stateMachineFactory;

    private final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> courseStateMachine;
    private final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> researchStateMachine;
    private final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> methodologyStateMachine;
    private  final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> activityStateMachine;
    private final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> chapterStateMachine;

    private final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> sectionStateMachine;
    private final StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> reportStateMachine;

    /**
     * get review history of the chapter
     *
     * @param chapterId the id of chapter
     * @return Review list of the chapter
     */
    @GetMapping("chapter/{chapterId}")
    @PreAuthorize("hasAuthority('course.review')")
    public ResponseEntity<List<EduReview>> getReviewListByRefId(@PathVariable Long chapterId){
        LambdaQueryWrapper<EduReview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduReview::getRefId,chapterId)
                .eq(EduReview::getRefType, ReviewType.CHAPTER)
                .orderByDesc(EduReview::getId);
        List<EduReview> reviewList = reviewService.list(wrapper);
        return ResUtils.ok(reviewList);
    }

    @GetMapping()
    public ResponseEntity<PageResponse<ReviewBasicDTO>> getPageReview(ListQueryParams params){
        PageResponse<ReviewBasicDTO> page = reviewService.getPageReview(params);
        return ResUtils.ok(page);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('Methodology.review')")
    public ResponseEntity<String> doReviewAction(@Valid @RequestBody ReviewApplyVo vo){
        log.info("review");
        stateMachineFactory.getStateMachine(vo.getType()).fireEvent(vo.getFrom(),vo.getAction(),vo);
//        switch(vo.getType()){
//            case COURSE -> courseStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
//            case CHAPTER-> chapterStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
//            case SECTION-> sectionStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
//            case METHODOLOGY -> methodologyStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
//            case ACTIVITY -> activityStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
//            case RESEARCH -> researchStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
//            case REPORT-> reportStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
//        }
        return ResUtils.ok();
    }
    @GetMapping(value = "{id}",params = {"type"})
    public ResponseEntity<List<ReviewDTO>> getReviewListByRefId(@PathVariable Long id, @RequestParam ReviewType type){
        List<ReviewDTO> reviews = reviewService.getReviewListByRefId(id,type);
        return ResUtils.ok(reviews);
    }

}
