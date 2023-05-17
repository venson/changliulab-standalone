package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.enums.PermissionAction;
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

    private String category;
    private PermissionAction action;

    private String name;

    private String slug;

    private String description;




}
