package com.venson.changliulab.entity.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AdminRoleDTO implements Serializable {

    private Long id;

    private String roleName;

    private String roleCode;

    private Boolean enable;

    private String remark;

    private List<Long> permissionIds;


}
