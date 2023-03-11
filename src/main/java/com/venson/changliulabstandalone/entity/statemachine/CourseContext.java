package com.venson.changliulabstandalone.entity.statemachine;

import com.venson.changliulabstandalone.entity.pojo.EduCourse;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
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
