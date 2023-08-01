package com.venson.changliulab.entity;

import com.venson.changliulab.entity.enums.UserType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Bo will be store in AuthContext(ThreadLocal)
 */
@Data
public class UserContextInfoBO implements Serializable {

    private Long id;

    private String username;

    private String email;

    private String token;

    private UserType type;

    private List<String> permissionValueList;


}
