package com.venson.changliulab.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.venson.changliulab.utils.ResUtils;
import com.venson.changliulab.entity.pojo.EduScholarCitation;
import com.venson.changliulab.service.admin.EduScholarCitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
public class EduScholarCitationController {
    private final EduScholarCitationService citationService;

    @GetMapping("{scholarId}")
    public ResponseEntity<List<EduScholarCitation>> getCitationByScholarId(@PathVariable String scholarId){
        List<EduScholarCitation> list = citationService.list(
                new QueryWrapper<EduScholarCitation>()
                        .select("year", "citations", "scholar_id")
                        .eq("scholar_id", scholarId));
        return  ResponseEntity.ok(list);
    }

    @PostMapping()
    public ResponseEntity<String> newScholarCitation(@RequestBody List<EduScholarCitation> citationList){
        citationService.saveBatch(citationList);
        return ResponseEntity.ok().build();
    }

    @PutMapping("{scholarId}")
    public ResponseEntity<String> updateCitation(@PathVariable String scholarId, @RequestBody List<EduScholarCitation> citationList){

        citationService.remove(new QueryWrapper<EduScholarCitation>().eq("scholar_id", scholarId));
        citationService.saveBatch(citationList);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{scholarId}")
    public ResponseEntity<String> deleteCitationByScholarId(@PathVariable String scholarId){
        citationService.remove(new QueryWrapper<EduScholarCitation>().eq("scholar_id", scholarId));
        return ResponseEntity.ok().build();
    }


}
