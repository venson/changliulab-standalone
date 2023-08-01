package com.venson.changliulab.config.security;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordNotFoundBCryptEncoded {
    public static final String instance = new BCryptPasswordEncoder().encode("user password not found");

    private PasswordNotFoundBCryptEncoded() {}
}
