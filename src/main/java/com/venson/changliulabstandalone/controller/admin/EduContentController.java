package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.dto.CourseSyllabusDTO;
import com.venson.changliulabstandalone.entity.vo.CourseTreeNodeVo;
import com.venson.changliulabstandalone.service.admin.EduChapterService;
import com.venson.changliulabstandalone.service.admin.EduContentService;
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
public class EduContentController {

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private EduContentService contentService;
    /**
     *  get the course Tree, use chapter and section as tree node
     * @param courseId the id of course
     * @return tree Node List
     */
    @GetMapping("{courseId}")
    public ResponseEntity<List<CourseTreeNodeVo>> getCourseTreeByCourseId(@PathVariable Long courseId){
        List<CourseTreeNodeVo> tree = chapterService.getCourseTreeByCourseId(courseId);
        return ResUtils.ok(tree);
    }

    @GetMapping("syllabus/{courseId}")
    public ResponseEntity<List<CourseSyllabusDTO>> getCourseSyllabusByCourseId(@PathVariable Long courseId){
        List<CourseSyllabusDTO> syllabus =contentService.getSyllabusByCourseId(courseId);
//        List<CourseSyllabusDTO> syllabus = chapterService.getSyllabusByCourseId(courseId);
        return ResUtils.ok(syllabus);
    }

}
