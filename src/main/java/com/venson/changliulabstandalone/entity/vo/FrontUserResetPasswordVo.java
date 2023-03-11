package com.venson.changliulabstandalone.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class FrontUserResetPasswordVo implements Serializable {
    private String username;
    private String email;
}
