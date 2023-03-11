package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.dto.AdminResearchDTO;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduResearch;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.dto.ResearchDTO;
import com.venson.changliulabstandalone.entity.enums.LanguageEnum;
import com.venson.changliulabstandalone.service.admin.EduResearchService;
import com.venson.changliulabstandalone.service.admin.EduReviewService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class EduResearchController {

    @Autowired
    private EduResearchService service;
    @Autowired
    private EduReviewService reviewService;

    @GetMapping("{page}/{limit}")
    @PreAuthorize("hasAuthority('research.list')")
    public Result<PageResponse<EduResearch>> getResearchPage(@PathVariable Integer page, @PathVariable Integer limit){
        PageResponse<EduResearch> pageRes = service.getResearchPage(page, limit);
        return Result.success(pageRes);
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('research.edit')")
    public Result<AdminResearchDTO> getResearch(@PathVariable Long id){
        AdminResearchDTO researchDTO = service.getResearchById(id);
        return Result.optional(researchDTO);
    }
    @GetMapping("preview/{id}")
    public Result<ResearchDTO> getResearchPreviewById(@PathVariable Long id){
        ResearchDTO preview = service.getResearchPreviewById(id);
        return Result.success(preview);
    }
    @PostMapping()
    @PreAuthorize("hasAuthority('research.add')")
    public Result<Long> addResearch(@Valid @RequestBody AdminResearchDTO research){
        Long id = service.addResearch(research);
        return Result.success(id);
    }
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('research.edit')")
    public Result<String> updateResearch(@PathVariable Long id,@Valid @RequestBody AdminResearchDTO research){
        service.updateResearch(id,research);
        return Result.success();
    }
    @DeleteMapping("{id}")
    public Result<String> removeResearch(@PathVariable Long id){
        service.removeResearchById(id);
        return Result.success();
    }

    @GetMapping("review/{id}")
    @PreAuthorize("hasAnyAuthority('research.review', 'research.review.request','research.review.pass','research.review.reject')")
    public Result<List<EduReview>> getReview(@PathVariable Long id){
        List<EduReview> reviewList = reviewService.getReviewByResearchId(id);
        return Result.success(reviewList);
    }
    @GetMapping("review/{current}/{size}")
    public Result<PageResponse<EduResearch>> getResearchReviewPage(@PathVariable Integer current, @PathVariable Integer size){
        PageResponse<EduResearch> page =service.getResearchReviewPage(current, size);
        return Result.success(page);
    }
    @GetMapping(value = "enable/{id}",params = {"lang"})
    public Result<String> switchEnableById(@PathVariable Long id, @RequestParam LanguageEnum lang){
        service.switchEnableById(id, lang);
        return Result.success();
    }

}
