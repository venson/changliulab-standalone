package com.venson.changliulab.controller.admin;

import com.venson.changliulab.entity.subject.SubjectTreeNode;
import com.venson.changliulab.service.admin.EduSubjectService;
import com.venson.changliulab.utils.ResUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-05-10
 */
@RestController
@RequestMapping("/eduservice/admin/edu-subject")
@Slf4j
@RequiredArgsConstructor
public class EduSubjectController {

    private final EduSubjectService eduSubjectService;


    @PostMapping("")
    public ResponseEntity<String> importSubjectFromExcel(@RequestPart(value = "file") MultipartFile file){
//         RequestPart value "file" should match the name of upload in frontend
        log.info(file.getOriginalFilename());

        eduSubjectService.saveSubject(file, eduSubjectService);
        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('Course.subject.READ')")
    public ResponseEntity<List<SubjectTreeNode>> getAllSubject(){
        List<SubjectTreeNode> tree = eduSubjectService.getAllSubject();
        return ResponseEntity.ok(tree);
    }
    @PutMapping()
    @PreAuthorize("hasAuthority('Course.subject.EDIT')")
    public ResponseEntity<String> editSubjectList(@RequestBody List<SubjectTreeNode> treeNodes){
        eduSubjectService.editSubjectListByTreeNodes(treeNodes);
        return ResponseEntity.ok().build();
    }

}
