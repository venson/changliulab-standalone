package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.dto.ChapterContentDTO;
import com.venson.changliulabstandalone.entity.front.dto.ChapterFrontDTO;
import com.venson.changliulabstandalone.service.admin.EduChapterService;
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
    public ResponseEntity<ChapterContentDTO> getChapterById(@PathVariable Long chapterId){
        ChapterContentDTO  chapterContentDTO= chapterService.getChapterDTOById(chapterId);
        return ResUtils.ok(chapterContentDTO);
    }
    @PostMapping("")
    @PreAuthorize("hasAuthority('course.edit.content')")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<Long> addChapter(@RequestBody ChapterFrontDTO chapterFrontDTO){
        Long id = chapterService.addChapter(chapterFrontDTO);
        return ResUtils.ok(id);
    }

    @PutMapping("{chapterId}")
    @PreAuthorize("hasAuthority('course.edit.content')")
    public ResponseEntity<String> updateChapterById(@PathVariable Long chapterId, @RequestBody ChapterFrontDTO chapter){
        chapterService.updateChapterById(chapterId,chapter);
        return ResUtils.ok();
    }



    /**
     * mark the chapter, will remove after review
     */
    @DeleteMapping("{chapterId}")
    @PreAuthorize("hasAuthority('course.edit.REMOVE')")
    public ResponseEntity<String> deleteChapterById(@PathVariable Long chapterId ){
        chapterService.removeMarkChapterById(chapterId);
        return ResUtils.ok();
    }



}
