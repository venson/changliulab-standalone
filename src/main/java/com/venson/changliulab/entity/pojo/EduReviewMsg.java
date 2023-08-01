package com.venson.changliulab.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewType;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author venson
 * @since 2023-01-25
 */
@Getter
@Setter
@TableName("edu_review_msg")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EduReviewMsg implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long reviewId;

    private Long refId;

    private ReviewType refType;

    private Long requestUserId;
    private String requestUsername;

    private String requestMsg;

    private Long reviewUserId;
    private String reviewUsername;

    private String reviewMsg;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    private ReviewAction reviewAction;

}
