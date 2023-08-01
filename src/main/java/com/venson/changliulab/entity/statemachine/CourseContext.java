package com.venson.changliulab.entity.statemachine;

import com.venson.changliulab.entity.pojo.EduCourse;
import com.venson.changliulab.entity.dto.ReviewApplyVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CourseContext implements Serializable {
    @Serial
    private static final long serialVersionUID = 23774596723457L;

    private EduCourse course;
    private ReviewApplyVo reviewVo;

}
