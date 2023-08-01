package com.venson.changliulab.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.venson.changliulab.entity.pojo.EduCourse;
import com.venson.changliulab.entity.dto.CoursePreviewVo;
import com.venson.changliulab.entity.front.vo.CourseFrontInfoDTO;
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
