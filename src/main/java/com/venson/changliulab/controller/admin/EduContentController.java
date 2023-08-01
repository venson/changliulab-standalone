package com.venson.changliulab.controller.admin;

import com.venson.changliulab.utils.ResUtils;
import com.venson.changliulab.entity.dto.CourseSyllabusDTO;
import com.venson.changliulab.entity.vo.CourseTreeNodeVo;
import com.venson.changliulab.service.admin.EduChapterService;
import com.venson.changliulab.service.admin.EduContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  Only get course chapter and section node tree.
 *  get ,edit and remove chapters are in EduChapterController
 *  get ,edit and remove section are in EduSectionController
 */
@RestController
@RequestMapping("/eduservice/admin/edu-content")
@RequiredArgsConstructor
public class EduContentController {

    private final EduChapterService chapterService;

    private final EduContentService contentService;
    /**
     *  get the course Tree, use chapter and section as tree node
     * @param courseId the id of course
     * @return tree Node List
     */
    @GetMapping("{courseId}")
    public ResponseEntity<List<CourseTreeNodeVo>> getCourseTreeByCourseId(@PathVariable Long courseId){
        List<CourseTreeNodeVo> tree = chapterService.getCourseTreeByCourseId(courseId);
        return ResponseEntity.ok(tree);
    }

    @GetMapping("syllabus/{courseId}")
    public ResponseEntity<List<CourseSyllabusDTO>> getCourseSyllabusByCourseId(@PathVariable Long courseId){
        List<CourseSyllabusDTO> syllabus =contentService.getSyllabusByCourseId(courseId);
//        List<CourseSyllabusDTO> syllabus = chapterService.getSyllabusByCourseId(courseId);
        return ResponseEntity.ok(syllabus);
    }

}
