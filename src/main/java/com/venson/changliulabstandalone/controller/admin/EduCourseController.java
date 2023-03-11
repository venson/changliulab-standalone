package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.pojo.EduCourse;
import com.venson.changliulabstandalone.entity.dto.CourseInfoDTO;
import com.venson.changliulabstandalone.entity.dto.CoursePageDTO;
import com.venson.changliulabstandalone.entity.dto.CoursePreviewVo;
import com.venson.changliulabstandalone.entity.enums.PageType;
import com.venson.changliulabstandalone.service.admin.EduCourseService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author baomidou
 * @since 2022-05-11
 */
@Tag(name = "EduCourseController", description = "后台课程管理")
@RestController
@RequestMapping("/eduservice/admin/edu-course")
@Slf4j
public class EduCourseController {

    private final EduCourseService courseService;

    public EduCourseController(EduCourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('course.edit.info')")
    public Result<Long> addCourse(@RequestBody CourseInfoDTO dto){
        Long id = courseService.addCourse(dto);
        return Result.success(id);
    }


    @GetMapping("{courseId}")
    public Result<CourseInfoDTO> getCourse(@PathVariable("courseId") Long id){
        CourseInfoDTO infoVo = courseService.getCourseById(id);
        return  Result.success(infoVo);
    }

    @PutMapping("{courseId}")
    @PreAuthorize("hasAuthority('course.edit.info')")
    public Result<String> updateCourse(@PathVariable Long courseId, @RequestBody CourseInfoDTO dto){
        courseService.updateCourse(dto);
        return Result.success();
    }



    @GetMapping("{current}/{size}")
    @PreAuthorize("hasAuthority('course.list')")
    public Result<PageResponse<EduCourse>> courseList(@PathVariable Integer current,
                                                      @PathVariable Integer size,
                                                      @RequestParam(required = false) String condition){
        PageResponse<EduCourse> pageRes = courseService.getPageCoursePublishVo(current, size, condition);
        return Result.success(pageRes);
    }


    /**
     * mark remove, remove will perform after review.
     * @param courseId the id of course
     * @return RMessage
     */
    @DeleteMapping("{courseId}")
    @PreAuthorize("hasAuthority('course.remove')")
    public Result<String> removeCourseById(@PathVariable Long courseId){
        courseService.setRemoveMarkByCourseById(courseId) ;
        return Result.success();
    }
    @GetMapping("preview/{courseId}")
    @PreAuthorize("hasAuthority('course.edit.preview')")
    public Result<CoursePreviewVo> getCoursePreviewById(@PathVariable Long courseId){
        CoursePreviewVo coursePreview = courseService.getCoursePreviewById(courseId);
        return Result.success(coursePreview);
    }
    @GetMapping(value = "{current}/{size}", params = {"type","condition"})
    public Result<PageResponse<CoursePageDTO>> getCoursePageList(@PathVariable Integer current,
                                                         @PathVariable Integer size,
                                                         @RequestParam PageType type,
                                                         @RequestParam(required = false) String condition){
        PageResponse<CoursePageDTO> page;
        if(PageType.REVIEW.equals(type)){
            page = courseService.getCoursePageReview(current, size);
        }else{
            page = courseService.getCoursePage(current,size, condition);
        }
        return Result.success(page);
    }

}
