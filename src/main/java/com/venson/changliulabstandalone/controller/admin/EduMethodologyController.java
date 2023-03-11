package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.vo.admin.AdminMethodologyVo;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduMethodology;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.dto.MethodologyDTO;
import com.venson.changliulabstandalone.service.admin.EduMethodologyService;
import com.venson.changliulabstandalone.service.admin.EduReviewService;
import com.venson.changliulabstandalone.valid.AddGroup;
import com.venson.changliulabstandalone.valid.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/eduservice/admin/edu-methodology")
public class EduMethodologyController {
    @Autowired
    private EduMethodologyService service;
    @Autowired
    private EduReviewService reviewService;

    @GetMapping("{page}/{limit}")
    @PreAuthorize("hasAuthority('methodology.list')")
    public Result<PageResponse<EduMethodology>> getMethodologyPage(@PathVariable Integer page, @PathVariable Integer limit){
        PageResponse<EduMethodology> pageRes = service.getMethodologyPage(page, limit);
        return Result.success(pageRes);
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('methodology.edit')")
    public Result<EduMethodology> getMethodology(@PathVariable Long id){
        EduMethodology methodology = service.getById(id);
        return Result.optional(methodology);
    }
    @GetMapping("view/{id}")
    public Result<MethodologyDTO> getMethodologyViewById(@PathVariable Long id){
        MethodologyDTO view = service.getMethodologyViewById(id);
        return Result.success(view);
    }
    @PostMapping()
    public Result<Long> addMethodology(@Validated(AddGroup.class) @RequestBody AdminMethodologyVo methodology){
        Long id  = service.addMethodology(methodology);
        return Result.success(id);
    }
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('methodology.edit')")
    public Result<String> updateMethodology(@PathVariable Long id, @Validated(UpdateGroup.class) @RequestBody AdminMethodologyVo methodology){
        service.updateMethodology(id,methodology);
        return Result.success();
    }

    @GetMapping("review/{id}")
    @PreAuthorize("hasAnyAuthority('methodology.review', 'methodology.review.request','methodology.review.pass','methodology.review.reject')")
    public Result<List<EduReview>> getReview(@PathVariable Long id){
        List<EduReview> reviewList = reviewService.getReviewByMethodologyId(id);
        return Result.success(reviewList);
    }
    @GetMapping("review/{current}/{size}")
    public Result<PageResponse<EduMethodology>> getMethodologyReviewPage(@PathVariable Integer current, @PathVariable Integer size){
        PageResponse<EduMethodology> page =service.getMethodologyReviewPage(current, size);
        return Result.success(page);
    }

    @PostMapping(value = "enable/{id}")
    public Result<String> switchEnableById(@PathVariable Long id){
        service.switchEnableById(id);
        return Result.success();
    }
    @PostMapping(value = "public/{id}")
    public Result<String> switchPublicById(@PathVariable Long id){
        service.switchPublicById(id);
        return Result.success();
    }


}
