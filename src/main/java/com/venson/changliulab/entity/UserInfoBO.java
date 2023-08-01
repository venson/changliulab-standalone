package com.venson.changliulab.entity;

import com.venson.changliulab.entity.enums.UserType;
import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class UserInfoBO implements Serializable {

    private String username;

    private Long id;

    private String email;

    private String password;

    private UserType type;
}
