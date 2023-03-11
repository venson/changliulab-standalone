package com.venson.changliulabstandalone.entity.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.venson.changliulabstandalone.entity.enums.LanguageEnum;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

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
@TableName("edu_research")
public class EduResearch implements Serializable {

    @Serial
    private static final long serialVersionUID = 1231253235L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String markdown;
    private String title;

    private String publishedMd;
    private String htmlBrBase64;
    private String publishedHtmlBrBase64;

    private LanguageEnum language;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    private Boolean isModified;

    private ReviewStatus review;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    private Boolean enable;
    private Boolean isRemoveAfterReview;

    private Boolean isPublished;

}
