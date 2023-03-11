package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduScholar;
import com.venson.changliulabstandalone.entity.dto.ScholarAdminDTO;
import com.venson.changliulabstandalone.entity.vo.ScholarFilterVo;
import com.venson.changliulabstandalone.service.admin.EduScholarService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @PreAuthorize("hasAuthority('scholar.edit')")
    public Result<ScholarAdminDTO> getScholar(@PathVariable Long id){
        ScholarAdminDTO scholar = scholarService.getScholarById(id);
        return Result.success(scholar);

    }

    @PostMapping("{page}/{limit}")
    @PreAuthorize("hasAuthority('scholar.list')")
    public Result<PageResponse<EduScholar>> getPageScholar(@PathVariable Integer page, @PathVariable Integer limit,
                                                           @RequestBody(required = false) ScholarFilterVo filterVo){
        PageResponse<EduScholar> pageRes = scholarService.getPageScholar(page, limit, filterVo);
        return Result.success(pageRes);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('scholar.add')")
    public Result<Long> addScholar(@RequestBody  ScholarAdminDTO scholar){
        Long id = scholarService.addScholar(scholar);
        return Result.success(id);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('scholar.edit')")
    public Result<String> updateScholar(@RequestBody ScholarAdminDTO scholar){
        scholarService.updateScholar(scholar);
        return Result.success();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('scholar.remove')")
    public Result<String> deleteScholar(@PathVariable Long id){
        scholarService.removeById(id);
        return Result.success();
    }

//  @
}
