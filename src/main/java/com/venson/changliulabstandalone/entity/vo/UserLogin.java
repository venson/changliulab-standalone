package com.venson.changliulabstandalone.entity.vo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserLogin implements Serializable {
    @Email
    private String username;
    @NotBlank
    private String password;
}
