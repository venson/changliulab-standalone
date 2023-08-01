package com.venson.changliulab.entity.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.enums.ReviewType;
import lombok.*;

/**
 * <p>
 *
 * </p>
 *
 * @author venson
 * @since 2022-07-16
 */
@Getter
@Setter
@TableName("edu_review")
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EduReview implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private ReviewStatus status;
    private ReviewType refType;
    private Long refId;
    private Long refParentId;
    private ReviewType refParentType;
    @Version
    private Long version;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;
}
