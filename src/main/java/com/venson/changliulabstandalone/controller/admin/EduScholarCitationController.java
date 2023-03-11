package com.venson.changliulabstandalone.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduScholarCitation;
import com.venson.changliulabstandalone.service.admin.EduScholarCitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author venson
 * @since 2022-06-20
 */
@RestController
@RequestMapping("/eduservice/admin/edu-scholar-citation")
public class EduScholarCitationController {
    @Autowired
    private EduScholarCitationService citationService;

    @GetMapping("{scholarId}")
    public Result<List<EduScholarCitation>> getCitationByScholarId(@PathVariable String scholarId){
        List<EduScholarCitation> list = citationService.list(
                new QueryWrapper<EduScholarCitation>()
                        .select("year", "citations", "scholar_id")
                        .eq("scholar_id", scholarId));
        return  Result.success(list);
    }

    @PostMapping()
    public Result<String> newScholarCitation(@RequestBody List<EduScholarCitation> citationList){
        citationService.saveBatch(citationList);
        return Result.success();
    }

    @PutMapping("{scholarId}")
    public Result<String> updateCitation(@PathVariable String scholarId, @RequestBody List<EduScholarCitation> citationList){

        citationService.remove(new QueryWrapper<EduScholarCitation>().eq("scholar_id", scholarId));
        citationService.saveBatch(citationList);
        return Result.success();
    }

    @DeleteMapping("{scholarId}")
    public Result<String> deleteCitationByScholarId(@PathVariable String scholarId){
        citationService.remove(new QueryWrapper<EduScholarCitation>().eq("scholar_id", scholarId));
        return Result.success();
    }


}
