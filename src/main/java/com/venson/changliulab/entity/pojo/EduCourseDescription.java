package com.venson.changliulab.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程简介
 * </p>
 *
 * @author baomidou
 * @since 2022-05-11
 */
@TableName("edu_course_description")
@Data
public class EduCourseDescription implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT,value = "id")
    private Long id;

    private String description;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


    @Override
    public String toString() {
        return "EduCourseDescription{" +
        "id=" + id +
        ", description=" + description +
        ", gmtCreate=" + gmtCreate +
        ", gmtModified=" + gmtModified +
        "}";
    }
}
