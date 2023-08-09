package com.venson.changliulab.controller.admin;

import com.venson.changliulab.entity.dto.ChapterContentDTO;
import com.venson.changliulab.entity.front.dto.ChapterFrontDTO;
import com.venson.changliulab.entity.vo.admin.CommonMetaVo;
import com.venson.changliulab.service.admin.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-05-11
 */
@RestController
@RequestMapping("/eduservice/admin/edu-chapter")
public class EduChapterController {

    private final EduChapterService chapterService;

    @Autowired
    public EduChapterController(EduChapterService chapterService) {
        this.chapterService = chapterService;
    }

    /**
     * get chapter and markdown by chapter ID
     * @param chapterId the id of chapter
     * @return chapter and markdown
     */
    @GetMapping("{chapterId}")
    @PreAuthorize("hasAnyAuthority('course.edit.content', 'course.edit.preview')")
    public ResponseEntity<ChapterContentDTO> getChapterById(@PathVariable Long chapterId, CommonMetaVo vo){
        ChapterContentDTO  chapterContentDTO= chapterService.getChapterDTOById(chapterId,vo);
        return ResponseEntity.ok(chapterContentDTO);
    }
    @PostMapping("")
    @PreAuthorize("hasAuthority('course.edit.content')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Long> addChapter(@RequestBody ChapterFrontDTO chapterFrontDTO){
        Long id = chapterService.addChapter(chapterFrontDTO);
        return ResponseEntity.ok(id);
    }

    @PutMapping("{chapterId}")
    @PreAuthorize("hasAuthority('course.edit.content')")
    public ResponseEntity<String> updateChapterById(@PathVariable Long chapterId, @RequestBody ChapterFrontDTO chapter){
        chapterService.updateChapterById(chapterId,chapter);
        return ResponseEntity.ok().build();
    }



    /**
     * mark the chapter, will remove after review
     */
    @DeleteMapping("{chapterId}")
    @PreAuthorize("hasAuthority('course.edit.REMOVE')")
    public ResponseEntity<String> deleteChapterById(@PathVariable Long chapterId ){
        chapterService.removeMarkChapterById(chapterId);
        return ResponseEntity.ok().build();
    }



}
