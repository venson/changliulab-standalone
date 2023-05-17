package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.dto.AdminResearchDTO;
import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduResearch;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.dto.ResearchDTO;
import com.venson.changliulabstandalone.service.admin.EduResearchService;
import com.venson.changliulabstandalone.service.admin.EduReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
//        return ResUtils.ok(res);
//    }

    @GetMapping("{page}/{limit}")
    @PreAuthorize("hasAuthority('Research.READ')")
    public ResponseEntity<PageResponse<EduResearch>> getResearchPage(@PathVariable Integer page, @PathVariable Integer limit){
        PageResponse<EduResearch> pageRes = service.getResearchPage(page, limit);
        return ResUtils.ok(pageRes);
    }
    @GetMapping()
    @PreAuthorize("hasAuthority('Research.READ')")
    public ResponseEntity<PageResponse<EduResearch>> getPage(PageQueryVo queryVo){
        PageResponse<EduResearch> pageRes = service.getResearchPage(queryVo);
        return ResUtils.ok(pageRes);
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('Research.READ')")
    public ResponseEntity<AdminResearchDTO> getResearch(@PathVariable Long id){
        AdminResearchDTO researchDTO = service.getResearchById(id);
        return ResUtils.optional(researchDTO);
    }
    @GetMapping("preview/{id}")
    @PreAuthorize("hasAuthority('Research.READ')")
    public ResponseEntity<ResearchDTO> getResearchPreviewById(@PathVariable Long id){
        ResearchDTO preview = service.getResearchPreviewById(id);
        return ResUtils.optional(preview);
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
        return ResUtils.ok();
    }
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Research.REMOVE')")
    public ResponseEntity<String> removeResearch(@PathVariable Long id){
        service.removeResearchById(id);
        return ResUtils.ok();
    }

    @GetMapping("review/{id}")
    @PreAuthorize("hasAuthority('Research.REVIEW_READ')")
    public ResponseEntity<List<EduReview>> getReview(@PathVariable Long id){
        List<EduReview> reviewList = reviewService.getReviewByResearchId(id);
        return ResUtils.optional(reviewList);
    }
    @GetMapping("review")
    @PreAuthorize("hasAuthority('Research.REVIEW_READ')")
    public ResponseEntity<PageResponse<EduResearch>> getResearchReviewPage(PageQueryVo vo){
        PageResponse<EduResearch> page =service.getResearchReviewPage(vo);
        return ResUtils.optional(page);
    }
    @GetMapping(value = "enable/{id}",params = {"lang"})
    @PreAuthorize("hasAuthority('Research.ENABLE')")
    public ResponseEntity<String> switchEnableById(@PathVariable Long id){
        service.switchEnableById(id);
        return ResUtils.ok();
    }

}
