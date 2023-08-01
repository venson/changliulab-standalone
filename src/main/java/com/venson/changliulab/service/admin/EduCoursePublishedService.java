package com.venson.changliulab.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.entity.pojo.EduCoursePublished;
import com.venson.changliulab.entity.front.dto.CourseFrontBriefDTO;
import com.venson.changliulab.entity.front.dto.CourseSyllabusFrontDTO;
import com.venson.changliulab.entity.front.vo.CourseFrontFilterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author venson
 * @since 2022-07-12
 */
public interface EduCoursePublishedService extends IService<EduCoursePublished> {

    PageResponse<EduCoursePublished> getFrontPageCourseList(Integer page, Integer limit, CourseFrontFilterVo courseFrontVo);



    List<CourseFrontBriefDTO> getFrontIndexCourse();

    List<CourseSyllabusFrontDTO> getSyllabusByCourseId(Long id);
}
