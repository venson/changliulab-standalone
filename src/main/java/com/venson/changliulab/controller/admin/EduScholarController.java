package com.venson.changliulab.controller.admin;

import com.venson.changliulab.entity.vo.admin.ListQueryParams;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.ResUtils;
import com.venson.changliulab.entity.pojo.EduScholar;
import com.venson.changliulab.entity.dto.ScholarAdminDTO;
import com.venson.changliulab.entity.vo.ScholarFilterVo;
import com.venson.changliulab.service.admin.EduScholarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author venson
 * @since 2022-06-18
 */
@RestController
@RequestMapping("/eduservice/admin/edu-scholar")
public class EduScholarController {

    @Autowired
    private EduScholarService scholarService;

//get Scholar article info

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('Scholar.READ')")
    public ResponseEntity<ScholarAdminDTO> getScholar(@PathVariable Long id){
        ScholarAdminDTO scholar = scholarService.getScholarById(id);
        return ResponseEntity.ok(scholar);

    }
    @GetMapping("")
    @PreAuthorize("hasAuthority('Scholar.READ')")
    public ResponseEntity<PageResponse<EduScholar>> getPage(ListQueryParams queryParams){
        PageResponse<EduScholar> pageRes = scholarService.getPageScholar(queryParams);
        return ResponseEntity.ok(pageRes);
    }

    @PostMapping("{page}/{limit}")
    @PreAuthorize("hasAuthority('Scholar.READ')")
    public ResponseEntity<PageResponse<EduScholar>> getPageScholar(@PathVariable Integer page, @PathVariable Integer limit,
                                                           @RequestBody(required = false) ScholarFilterVo filterVo){
        PageResponse<EduScholar> pageRes = scholarService.getPageScholar(page, limit, filterVo);
        return ResponseEntity.ok(pageRes);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('Scholar.CREATE')")
    public ResponseEntity<Long> addScholar(@RequestBody  ScholarAdminDTO scholar){
        Long id = scholarService.addScholar(scholar);
        return ResponseEntity.ok(id);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('Scholar.EDIT')")
    public ResponseEntity<String> updateScholar(@RequestBody ScholarAdminDTO scholar){
        scholarService.updateScholar(scholar);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Scholar.REMOVE')")
    public ResponseEntity<String> deleteScholar(@PathVariable Long id){
        scholarService.removeById(id);
        return ResponseEntity.ok().build();
    }

//  @
}
