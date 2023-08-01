package com.venson.changliulab.entity.vo;

import lombok.Data;

@Data
public class RegistrationVo {
    private String username;
    private String email;
    private String password;
    private String securityCode;
    private String avatar;
}
