package com.venson.changliulabstandalone.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
@Data
@TableName("edu_activity")
public class EduActivity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private String title;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    private String activityDate;

    private Long authorMemberId;

    private String authorMemberName;


    private Boolean isPublished;

    private Boolean isModified;

    private ReviewStatus review;

    @Version
    private Long version;

    private Boolean isRemoveAfterReview;
    private Boolean enable;


}
