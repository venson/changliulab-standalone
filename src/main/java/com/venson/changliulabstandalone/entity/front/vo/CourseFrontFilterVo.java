package com.venson.changliulabstandalone.entity.front.vo;

import com.venson.changliulabstandalone.entity.enums.CourseConditionSort;
import lombok.Data;

@Data
public class CourseFrontFilterVo {
    private Long subjectParentId;
    private Long subjectId;
    private CourseConditionSort sort;
    private Boolean ascend;
}
