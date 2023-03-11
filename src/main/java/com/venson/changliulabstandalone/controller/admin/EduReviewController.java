package com.venson.changliulabstandalone.controller.admin;

import com.alibaba.cola.statemachine.StateMachine;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.service.admin.EduReviewService;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
import com.venson.changliulabstandalone.entity.dto.ReviewDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewAction;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.enums.ReviewType;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EduReviewController {



    private final EduReviewService reviewService;

    @Autowired
    private StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> courseStateMachine;
    @Autowired
    private StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> researchStateMachine;
    @Autowired
    private StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> methodologyStateMachine;
    @Autowired
    private StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> activityStateMachine;
    @Autowired
    private StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> chapterStateMachine;

    @Autowired
    private StateMachine<ReviewStatus, ReviewAction,ReviewApplyVo> sectionStateMachine;
    public EduReviewController(EduReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * get review history of the chapter
     *
     * @param chapterId the id of chapter
     * @return Review list of the chapter
     */
    @GetMapping("chapter/{chapterId}")
    @PreAuthorize("hasAuthority('course.review')")
    public Result<List<EduReview>> getReviewListByRefId(@PathVariable Long chapterId){
        LambdaQueryWrapper<EduReview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduReview::getRefId,chapterId)
                .eq(EduReview::getRefType, ReviewType.CHAPTER)
                .orderByDesc(EduReview::getId);
        List<EduReview> reviewList = reviewService.list(wrapper);
        return Result.success(reviewList);
    }

    @GetMapping()
    public Result<String> test(){
        return Result.success();
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('methodology.review')")
    public Result<String> doReviewAction(@Valid @RequestBody ReviewApplyVo vo){
        log.info("review");
        switch(vo.getType()){
            case COURSE -> courseStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
            case CHAPTER-> chapterStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
            case SECTION-> sectionStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
            case METHODOLOGY -> methodologyStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
            case ACTIVITY -> activityStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
            case RESEARCH -> researchStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
        }
        return Result.success();
    }
    @GetMapping(value = "{id}",params = {"type"})
    public Result<List<ReviewDTO>> getReviewListByRefId(@PathVariable Long id, @RequestParam ReviewType type){
        List<ReviewDTO> reviews = reviewService.getReviewListByRefId(id,type);
        return Result.success(reviews);
    }

}
