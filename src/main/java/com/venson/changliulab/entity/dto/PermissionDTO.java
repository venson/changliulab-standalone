package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.PermissionAction;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class PermissionDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 123124324970773L;
    private String category;
    private List<PermissionAction> action;
}
