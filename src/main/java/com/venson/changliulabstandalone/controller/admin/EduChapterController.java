package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.dto.ChapterContentDTO;
import com.venson.changliulabstandalone.entity.front.dto.ChapterFrontDTO;
import com.venson.changliulabstandalone.service.admin.EduChapterService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Result<ChapterContentDTO> getChapterById(@PathVariable Long chapterId){
        ChapterContentDTO  chapterContentDTO= chapterService.getChapterDTOById(chapterId);
        return Result.success(chapterContentDTO);
    }
    @PostMapping("")
    @PreAuthorize("hasAuthority('course.edit.content')")
    @Transactional(rollbackFor = Exception.class)
    public Result<Long> addChapter(@RequestBody ChapterFrontDTO chapterFrontDTO){
        Long id = chapterService.addChapter(chapterFrontDTO);
        return Result.success(id);
    }

    @PutMapping("{chapterId}")
    @PreAuthorize("hasAuthority('course.edit.content')")
    public Result<String> updateChapterById(@PathVariable Long chapterId, @RequestBody ChapterFrontDTO chapter){
        chapterService.updateChapterById(chapterId,chapter);
        return Result.success();
    }



    /**
     * mark the chapter, will remove after review
     */
    @DeleteMapping("{chapterId}")
    @PreAuthorize("hasAuthority('course.edit.remove')")
    public Result<String> deleteChapterById(@PathVariable Long chapterId ){
        chapterService.removeMarkChapterById(chapterId);
        return Result.success();
    }



}
