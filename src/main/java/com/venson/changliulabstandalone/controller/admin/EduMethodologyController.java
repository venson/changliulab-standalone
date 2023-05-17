package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.vo.admin.AdminMethodologyVo;
import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduMethodology;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.dto.MethodologyDTO;
import com.venson.changliulabstandalone.service.admin.EduMethodologyService;
import com.venson.changliulabstandalone.service.admin.EduReviewService;
import com.venson.changliulabstandalone.valid.AddGroup;
import com.venson.changliulabstandalone.valid.UpdateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    @PreAuthorize("hasAuthority('Methodology.READ')")
    public ResponseEntity<PageResponse<EduMethodology>> getMethodologyPage(@PathVariable Integer page, @PathVariable Integer limit){
        PageResponse<EduMethodology> pageRes = service.getMethodologyPage(page, limit);
        return ResUtils.ok(pageRes);
    }
    @GetMapping()
    @PreAuthorize("hasAuthority('Methodology.READ')")
    public ResponseEntity<PageResponse<EduMethodology>> getPage(PageQueryVo vo){
        PageResponse<EduMethodology> pageRes = service.getMethodologyPage(vo);
        return ResUtils.ok(pageRes);
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('Methodology.EDIT')")
    public ResponseEntity<EduMethodology> getMethodology(@PathVariable Long id){
        EduMethodology methodology = service.getById(id);
        return ResUtils.ok(methodology);
    }
    @GetMapping("view/{id}")
    @PreAuthorize("hasAuthority('Methodology.READ')")
    public ResponseEntity<MethodologyDTO> getMethodologyViewById(@PathVariable Long id){
        MethodologyDTO view = service.getMethodologyViewById(id);
        return ResUtils.ok(view);
    }
    @PostMapping()
    @PreAuthorize("hasAuthority('Methodology.CREATE')")
    public ResponseEntity<Long> addMethodology(@Validated(AddGroup.class) @RequestBody AdminMethodologyVo methodology){
        Long id  = service.addMethodology(methodology);
        return ResUtils.ok(id);
    }
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('Methodology.EDIT')")
    public ResponseEntity<String> updateMethodology(@PathVariable Long id, @Validated(UpdateGroup.class) @RequestBody AdminMethodologyVo methodology){
        service.updateMethodology(id,methodology);
        return ResUtils.ok();
    }

    @GetMapping("review/{id}")
    @PreAuthorize("hasAuthority('Methodology.REVIEW_READ')")
    public ResponseEntity<List<EduReview>> getReview(@PathVariable Long id){
        List<EduReview> reviewList = reviewService.getReviewByMethodologyId(id);
        return ResUtils.ok(reviewList);
    }
    @GetMapping("review/{current}/{size}")
    @PreAuthorize("hasAuthority('Methodology.REVIEW_READ')")
    public ResponseEntity<PageResponse<EduMethodology>> getMethodologyReviewPage(@PathVariable Integer current, @PathVariable Integer size){
        PageResponse<EduMethodology> page =service.getMethodologyReviewPage(current, size);
        return ResUtils.ok(page);
    }

    @PostMapping(value = "enable/{id}")
    @PreAuthorize("hasAuthority('Methodology.ENABLE')")
    public ResponseEntity<String> switchEnableById(@PathVariable Long id){
        service.switchEnableById(id);
        return ResUtils.ok();
    }
    @PostMapping(value = "public/{id}")
    @PreAuthorize("hasAuthority('Methodology.PUBLIC')")
    public ResponseEntity<String> switchPublicById(@PathVariable Long id){
        service.switchPublicById(id);
        return ResUtils.ok();
    }
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Methodology.REMOVE')")
    public ResponseEntity<String> removeMethodology(@PathVariable Long id){
        service.removeMethodologyById(id);
        return ResUtils.ok();
    }


}
