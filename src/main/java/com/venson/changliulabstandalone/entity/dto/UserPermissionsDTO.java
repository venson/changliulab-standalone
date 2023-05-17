package com.venson.changliulabstandalone.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class UserPermissionsDTO implements Serializable {
    private Long id;
    private List<String> permissions;
}
