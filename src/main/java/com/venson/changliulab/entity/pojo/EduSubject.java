package com.venson.changliulab.entity.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程科目
 * </p>
 *
 * @author baomidou
 * @since 2022-05-10
 */
@TableName("edu_subject")
@Data
public class EduSubject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String title;

    private Long parentId;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


    @Override
    public String toString() {
        return "EduSubject{" +
        "id=" + id +
        ", title=" + title +
        ", parentId=" + parentId +
        ", sort=" + sort +
        ", gmtCreate=" + gmtCreate +
        ", gmtModified=" + gmtModified +
        "}";
    }
}
