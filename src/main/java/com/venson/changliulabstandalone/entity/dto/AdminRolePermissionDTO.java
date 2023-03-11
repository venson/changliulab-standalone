package com.venson.changliulabstandalone.entity.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class AdminRolePermissionDTO implements Serializable {

    private Long id;

    private String roleName;

    private String roleCode;

    private String remark;

    private Long[] permissionIds;

    private Boolean permissionChanged;

}
