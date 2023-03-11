package com.venson.changliulabstandalone.entity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Data
public class AdminPermissionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID=239982354765L;

    private Long id;

    private Long pid;

    private String name;

//    //    0, 根菜单，1菜单，2 按钮
//    private Integer type;

    private String permissionValue;

    private String path;

    private String component;

    private String icon;

////    0: abandon 1:normal
//    private Integer status;
    private Integer level;

    private List<AdminPermissionDTO> children;

    private boolean isSelect;



}
