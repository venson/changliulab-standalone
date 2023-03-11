package com.venson.changliulabstandalone.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.venson.changliulabstandalone.entity.pojo.EduCoursePublished;
import com.venson.changliulabstandalone.entity.front.dto.CourseFrontBriefDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author venson
 * @since 2022-07-12
 */
@Mapper
public interface EduCoursePublishedMapper extends BaseMapper<EduCoursePublished> {


    List<CourseFrontBriefDTO> getFrontIndexCourse();
}
