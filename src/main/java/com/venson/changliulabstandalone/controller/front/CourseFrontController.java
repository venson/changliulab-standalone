package com.venson.changliulabstandalone.controller.front;

import com.venson.changliulabstandalone.entity.pojo.EduCoursePublished;
import com.venson.changliulabstandalone.service.admin.EduCoursePublishedService;
import com.venson.changliulabstandalone.service.admin.EduSubjectService;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.entity.front.dto.SectionFrontDTO;
import com.venson.changliulabstandalone.entity.front.vo.CourseFrontFilterVo;
import com.venson.changliulabstandalone.entity.front.vo.CourseFrontInfoDTO;
import com.venson.changliulabstandalone.entity.front.vo.CourseFrontTreeNodeVo;
import com.venson.changliulabstandalone.entity.subject.SubjectTreeNode;
import com.venson.changliulabstandalone.service.front.CourseFrontService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eduservice/front/course")
@Slf4j
public class CourseFrontController {
    @Autowired
    private CourseFrontService courseFrontService;
    @Autowired
    private EduCoursePublishedService coursePublishedService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    @Autowired
    private EduSubjectService subjectService;

    @GetMapping("{id}/{page}/{limit}")
    public Result<PageResponse<EduCoursePublished>> getFrontPageCourseByMemberId(@PathVariable Long id,
                                                                                 @PathVariable Integer page,
                                                                                 @PathVariable Integer limit){
        PageResponse<EduCoursePublished> pageRes = courseFrontService.getPageCourseByMemberId(id, page, limit);
        return Result.success(pageRes);
    }



    @PostMapping("{page}/{limit}")
    public Result<PageResponse<EduCoursePublished>> getFrontPageCourseList(@PathVariable Integer page,
                                                                           @PathVariable Integer limit,
                                                                           @RequestBody(required = false) CourseFrontFilterVo courseFrontVo){

        PageResponse<EduCoursePublished> pageRes = coursePublishedService.getFrontPageCourseList(page, limit, courseFrontVo);
        return Result.success(pageRes);
    }

    @GetMapping("{id}")
    public Result<CourseFrontInfoDTO> getFrontCourseInfo(@PathVariable Long id){
        CourseFrontInfoDTO courseInfoDTO = courseFrontService.getFrontCourseInfo(id);
        return Result.success(courseInfoDTO);
    }
    @GetMapping("section/{id}")
    @PostAuthorize("returnObject.data.isPublic or hasAuthority('user')")
    public Result<SectionFrontDTO> getSectionBySectionId(@PathVariable Long id){
        SectionFrontDTO section = courseFrontService.getSectionBySectionId(id);
        return Result.success(section);
    }
    @GetMapping("tree/{id}")
    public Result<List<CourseFrontTreeNodeVo>> getCourseTreeById(@PathVariable Long id){
        List<CourseFrontTreeNodeVo> tree = courseFrontService.getCourseFrontTreeByCourseId(id);
        String redisKey = "course:counter:" + id;
        CourseFrontInfoDTO frontCourseInfo = courseFrontService.getFrontCourseInfo(id);
        stringRedisTemplate.opsForValue().setIfAbsent(redisKey,frontCourseInfo.getViewCount().toString());
        stringRedisTemplate.opsForValue().increment(redisKey);
        return Result.success(tree);
    }
    @GetMapping("subject")
    public Result<List<SubjectTreeNode>> getAllSubject(){
        List<SubjectTreeNode> tree = subjectService.getAllSubject();

        return Result.success(tree);
    }
}
