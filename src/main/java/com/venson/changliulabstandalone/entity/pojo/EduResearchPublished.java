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
@TableName("edu_research_published")
public class EduResearchPublished implements Serializable {

    @Serial
    private static final long serialVersionUID = 12312525L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String markdown;
    private String title;

    private String htmlBrBase64;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    private Boolean enable;

}
