package com.venson.changliulabstandalone.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.venson.changliulabstandalone.entity.pojo.EduCourse;
import com.venson.changliulabstandalone.entity.dto.CoursePreviewVo;
import com.venson.changliulabstandalone.entity.front.vo.CourseFrontInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-05-11
 */
@Mapper
public interface EduCourseMapper extends BaseMapper<EduCourse> {
//    CoursePublishVo getPublishCourseInfo(Long courseId);

    List<CoursePreviewVo> getPublishCourseInfo(@Param(Constants.WRAPPER) Wrapper<CoursePreviewVo> wrapper);
    CoursePreviewVo getCoursePreviewById(Long id);

    CourseFrontInfoDTO getFrontCourseInfo(Long id);


}
