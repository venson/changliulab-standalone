package com.venson.changliulabstandalone.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.venson.changliulabstandalone.entity.enums.LanguageEnum;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
@Getter
@Setter
@TableName("edu_methodology_published")
public class EduMethodologyPublished implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234724L;

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private String markdown;

    private String htmlBrBase64;
    private String title;
    private Boolean enable;

    private Boolean isPublic;


    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
