package com.venson.changliulab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 123123L;
    private String email;
    private String randomPassword;
}
