package com.venson.changliulab.entity.front.vo;

import com.venson.changliulab.entity.enums.CourseConditionSort;
import lombok.Data;

@Data
public class CourseFrontFilterVo {
    private Long subjectParentId;
    private Long subjectId;
    private CourseConditionSort sort;
    private Boolean ascend;
}
