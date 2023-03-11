package com.venson.changliulabstandalone.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Data
@Accessors(chain = true)
@TableName("acl_permission")
public class AdminPermission implements Serializable {

    @Serial
    private static final long serialVersionUID = 28349283178095L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    private Long pid;

    private String name;

//    0, 根菜单，1菜单，2 按钮
    private Integer type;

    private String permissionValue;

    private String path;

    private String component;

    private String icon;

    private Integer status;



    @TableLogic
    private Boolean isDeleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdminPermission that = (AdminPermission) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
