package com.venson.changliulab.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.venson.changliulab.entity.dto.ReviewApplyVo;
import com.venson.changliulab.entity.dto.ReviewBasicDTO;
import com.venson.changliulab.entity.dto.ReviewDTO;
import com.venson.changliulab.entity.dto.ReviewTargetDTO;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.enums.ReviewType;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.entity.vo.admin.ListQueryParams;
import com.venson.changliulab.entity.vo.admin.ReviewMetaVo;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;
import com.venson.changliulab.entity.vo.admin.ReviewVo;
import com.venson.changliulab.service.admin.EduReviewService;
import com.venson.changliulab.statemachine.StateMachineFactory;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.ResUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    private final StateMachineFactory<ReviewStatus, ReviewAction, ReviewSMContext> stateMachineFactory;


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
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping()
    public ResponseEntity<PageResponse<ReviewBasicDTO>> getPageReview(ListQueryParams params){
        PageResponse<ReviewBasicDTO> page = reviewService.getPageReview(params);
        return ResponseEntity.ok(page);
    }

//    @PostMapping("")
//    @PreAuthorize("hasAuthority('Methodology.review')")
//    public ResponseEntity<String> doReviewAction(@Valid @RequestBody ReviewApplyVo vo){
//        log.info("review");
//        stateMachineFactory.getStateMachine(vo.getType()).fireEvent(vo.getFrom(),vo.getAction(),vo);
////        switch(vo.getType()){
////            case COURSE -> courseStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
////            case CHAPTER-> chapterStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
////            case SECTION-> sectionStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
////            case METHODOLOGY -> methodologyStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
////            case ACTIVITY -> activityStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
////            case RESEARCH -> researchStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
////            case REPORT-> reportStateMachine.fireEvent(vo.getFrom(),vo.getAction(),vo);
////        }
//        return ResponseEntity.ok().build();
//    }

        @PostMapping("")
    public ResponseEntity<String> doReviewAction(@Valid @RequestBody ReviewVo vo){
        log.info("review");
        stateMachineFactory.dispatch(vo);
        return ResponseEntity.ok().build();
    }
    @GetMapping("{id}")
//    public ResponseEntity<ReviewTargetDTO<ReviewAble>> getReviewById(@PathVariable  Long id, ReviewMetaVo metaVo){
        public ResponseEntity<ReviewTargetDTO<ReviewAble>> getReviewById(@PathVariable  Long id, @RequestParam(required = false) ReviewType type){
//        log.info(id.toString(), metaVo.toString());
//        log.info(metaVo.toString());
        log.info(type.toString());
        ReviewMetaVo metaVo = new ReviewMetaVo(type,null,null);
//        console.log(metaVo)
//        ReviewDTO<ReviewAble> review = reviewService.getReviewById(id,metaVo);
        ReviewTargetDTO<ReviewAble> review = reviewService.getReviewByTargetId(id,metaVo);
        return ResponseEntity.ok(review);
    }

}
