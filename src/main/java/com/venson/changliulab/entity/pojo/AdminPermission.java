package com.venson.changliulab.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.venson.changliulab.entity.enums.PermissionAction;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Getter
@Setter
@TableName("acl_permission")
public class AdminPermission implements Serializable {

    @Serial
    private static final long serialVersionUID = 28349283178095L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;


    private String name;
    private String category;


    private String slug;


    private PermissionAction action;

    private Boolean enable;

    private String description;



    @TableLogic
    private Boolean isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

}
