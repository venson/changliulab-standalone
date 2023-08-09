package com.venson.changliulab.controller.admin;

import com.venson.changliulab.entity.dto.AdminResearchDTO;
import com.venson.changliulab.entity.dto.ResearchPreviewDTO;
import com.venson.changliulab.entity.pojo.EduResearch;
import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.entity.vo.admin.CommonMetaVo;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.service.admin.EduResearchService;
import com.venson.changliulab.service.admin.EduReviewService;
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
 *  前端控制器
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
@RestController
@RequestMapping("/eduservice/admin/edu-research")
@Slf4j
@RequiredArgsConstructor
public class EduResearchController {

    private final EduResearchService service;
    private final EduReviewService reviewService;

//    @GetMapping()
//    public ResponseEntity<PageResponse<EduResearch>> getResearchPage(PageQueryVo pageQueryVo){
//        PageResponse<EduResearch> res =  service.getResearchPage(pageQueryVo);
//        return ResponseEntity.ok(res);
//    }

    @GetMapping("{page}/{limit}")
    @PreAuthorize("hasAuthority('Research.READ')")
    public ResponseEntity<PageResponse<EduResearch>> getResearchPage(@PathVariable Integer page, @PathVariable Integer limit){
        PageResponse<EduResearch> pageRes = service.getResearchPage(page, limit);
        return ResponseEntity.ok(pageRes);
    }
    @GetMapping()
    @PreAuthorize("hasAuthority('Research.READ')")
    public ResponseEntity<PageResponse<EduResearch>> getPage(PageQueryVo queryVo){
        PageResponse<EduResearch> pageRes = service.getResearchPage(queryVo);
        return ResponseEntity.ok(pageRes);
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('Research.READ')")
    public ResponseEntity<AdminResearchDTO> getResearch(@PathVariable Long id, CommonMetaVo vo){
        AdminResearchDTO researchDTO = service.getResearchById(id,vo);
        return ResponseEntity.ok(researchDTO);
    }
    @GetMapping("preview/{id}")
    @PreAuthorize("hasAuthority('Research.READ')")
    public ResponseEntity<ResearchPreviewDTO> getResearchPreviewById(@PathVariable Long id){
        ResearchPreviewDTO preview = service.getResearchPreviewById(id);
        return ResponseEntity.ok(preview);
    }
    @PostMapping()
    @PreAuthorize("hasAuthority('Research.CREATE')")
    public ResponseEntity<Long> addResearch(@Valid @RequestBody AdminResearchDTO research){
        Long id = service.addResearch(research);
        return ResUtils.created(id);
    }
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('Research.EDIT')")
    public ResponseEntity<String> updateResearch(@PathVariable Long id,@Valid @RequestBody AdminResearchDTO research){
        service.updateResearch(id,research);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Research.REMOVE')")
    public ResponseEntity<String> removeResearch(@PathVariable Long id){
        service.removeResearchById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("review/{id}")
    @PreAuthorize("hasAuthority('Research.REVIEW_READ')")
    public ResponseEntity<List<EduReview>> getReview(@PathVariable Long id){
        List<EduReview> reviewList = reviewService.getReviewByResearchId(id);
        return ResponseEntity.ok(reviewList);
    }
    @GetMapping("review")
    @PreAuthorize("hasAuthority('Research.REVIEW_READ')")
    public ResponseEntity<PageResponse<EduResearch>> getResearchReviewPage(PageQueryVo vo){
        PageResponse<EduResearch> page =service.getResearchReviewPage(vo);
        return ResponseEntity.ok(page);
    }
    @GetMapping(value = "enable/{id}",params = {"lang"})
    @PreAuthorize("hasAuthority('Research.ENABLE')")
    public ResponseEntity<String> switchEnableById(@PathVariable Long id){
        service.switchEnableById(id);
        return ResponseEntity.ok().build();
    }

}
