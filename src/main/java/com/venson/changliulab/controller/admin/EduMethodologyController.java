package com.venson.changliulab.controller.admin;

import com.venson.changliulab.entity.vo.admin.AdminMethodologyVo;
import com.venson.changliulab.entity.vo.admin.CommonMetaVo;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.entity.pojo.EduMethodology;
import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.entity.dto.MethodologyPreviewDTO;
import com.venson.changliulab.service.admin.EduMethodologyService;
import com.venson.changliulab.service.admin.EduReviewService;
import com.venson.changliulab.valid.AddGroup;
import com.venson.changliulab.valid.UpdateGroup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RequiredArgsConstructor
public class EduMethodologyController {
    private final EduMethodologyService service;
    private final EduReviewService reviewService;

    @GetMapping("{page}/{limit}")
    @PreAuthorize("hasAuthority('Methodology.READ')")
    public ResponseEntity<PageResponse<EduMethodology>> getMethodologyPage(@PathVariable Integer page, @PathVariable Integer limit){
        PageResponse<EduMethodology> pageRes = service.getMethodologyPage(page, limit);
        return ResponseEntity.ok(pageRes);
    }
    @GetMapping()
    @PreAuthorize("hasAuthority('Methodology.READ')")
    public ResponseEntity<PageResponse<EduMethodology>> getPage(PageQueryVo vo){
        PageResponse<EduMethodology> pageRes = service.getMethodologyPage(vo);
        return ResponseEntity.ok(pageRes);
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('Methodology.EDIT')")
    public ResponseEntity<EduMethodology> getMethodology(@PathVariable Long id, CommonMetaVo vo){
        log.info(vo.toString());
        EduMethodology methodology = service.getMethodologyById(id,vo);
        return ResponseEntity.ok(methodology);
    }
    @GetMapping("view/{id}")
    @PreAuthorize("hasAuthority('Methodology.READ')")
    public ResponseEntity<MethodologyPreviewDTO> getMethodologyViewById(@PathVariable Long id){
        MethodologyPreviewDTO view = service.getMethodologyViewById(id);
        return ResponseEntity.ok(view);
    }
    @PostMapping()
    @PreAuthorize("hasAuthority('Methodology.CREATE')")
    public ResponseEntity<Long> addMethodology(@Validated(AddGroup.class) @RequestBody AdminMethodologyVo methodology){
        Long id  = service.addMethodology(methodology);
        return ResponseEntity.ok(id);
    }
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('Methodology.EDIT')")
    public ResponseEntity<String> updateMethodology(@PathVariable Long id, @Validated(UpdateGroup.class) @RequestBody AdminMethodologyVo methodology){
        service.updateMethodology(id,methodology);
        return ResponseEntity.ok().build();
    }

    @GetMapping("review/{id}")
    @PreAuthorize("hasAuthority('Methodology.REVIEW_READ')")
    public ResponseEntity<List<EduReview>> getReview(@PathVariable Long id){
        List<EduReview> reviewList = reviewService.getReviewByMethodologyId(id);
        return ResponseEntity.ok(reviewList);
    }
    @GetMapping("review/{current}/{size}")
    @PreAuthorize("hasAuthority('Methodology.REVIEW_READ')")
    public ResponseEntity<PageResponse<EduMethodology>> getMethodologyReviewPage(@PathVariable Integer current, @PathVariable Integer size){
        PageResponse<EduMethodology> page =service.getMethodologyReviewPage(current, size);
        return ResponseEntity.ok(page);
    }

    @PostMapping(value = "enable/{id}")
    @PreAuthorize("hasAuthority('Methodology.ENABLE')")
    public ResponseEntity<String> switchEnableById(@PathVariable Long id){
        service.switchEnableById(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping(value = "public/{id}")
    @PreAuthorize("hasAuthority('Methodology.PUBLIC')")
    public ResponseEntity<String> switchPublicById(@PathVariable Long id){
        service.switchPublicById(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Methodology.REMOVE')")
    public ResponseEntity<String> removeMethodology(@PathVariable Long id){
        service.removeMethodologyById(id);
        return ResponseEntity.ok().build();
    }


}
