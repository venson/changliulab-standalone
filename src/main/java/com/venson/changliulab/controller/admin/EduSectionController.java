package com.venson.changliulab.controller.admin;

import com.venson.changliulab.utils.ResUtils;
import com.venson.changliulab.entity.dto.SectionContentDTO;
import com.venson.changliulab.entity.dto.SectionPreviewDTO;
import com.venson.changliulab.service.admin.EduSectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author venson
 * @since 2022-06-13
 */
@RestController
@RequestMapping("/eduservice/admin/edu-section")
@Slf4j
public class EduSectionController {



    @Autowired
    private  EduSectionService sectionService;


    /**
     * get section and markdown by section ID
     * @param sectionId the id of section
     * @return section and markdown
     */
    @GetMapping("{sectionId}")
    @PreAuthorize("hasAnyAuthority('course.edit.content', 'course.edit.preview')")
    public ResponseEntity<SectionContentDTO> getSectionById(@PathVariable Long sectionId){
        SectionContentDTO section = sectionService.getSectionById(sectionId);
        return ResponseEntity.ok(section);
    }
    @Transactional
    @PostMapping("")
    @PreAuthorize("hasAuthority('course.edit.content')")
    public ResponseEntity<Long> addSectionById(@RequestBody SectionContentDTO section){
        Long id = sectionService.addSection(section);
        log.info(String.valueOf(id));
        return ResponseEntity.ok(id);
    }

    @GetMapping("preview/{id}")
    @PreAuthorize("hasAuthority('course.edit.preview')")
    public ResponseEntity<SectionPreviewDTO> getSectionPreviewBySectionId(@PathVariable Long id){
        SectionPreviewDTO previewDTO = sectionService.getSectionPreviewBySectionId(id);
        return ResponseEntity.ok(previewDTO);

    }
    @PutMapping("{sectionId}")
    @PreAuthorize("hasAuthority('course.edit.content')")
    public ResponseEntity<String> updateSectionById(@PathVariable Long sectionId, @RequestBody SectionContentDTO sectionContentDTO){
        sectionService.updateSectionById(sectionId, sectionContentDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{sectionId}")
    public ResponseEntity<String> deleteSectionById(@PathVariable Long sectionId ){
        sectionService.removeMarkSectionById(sectionId);
        return ResponseEntity.ok().build();

    }

}
