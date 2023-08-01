package com.venson.changliulab.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.venson.changliulab.entity.enums.MemberLevel;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 讲师
 * </p>
 *
 * @author baomidou
 * @since 2022-05-02
 */
@TableName("edu_member")
@Data
@ToString
public class EduMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 123492835L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private String intro;

    private String career;

    private MemberLevel level;

    private String avatar;

    private String title;

    private Integer sort;

    @TableLogic
    private Integer isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

}
