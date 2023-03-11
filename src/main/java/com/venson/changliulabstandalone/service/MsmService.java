package com.venson.changliulabstandalone.service;

import jakarta.mail.MessagingException;

public interface MsmService {
    void sendCode(String emailUrl, String code, String codeName, String mailPurpose, Integer expire) throws MessagingException;

    void sendResetPasswordEmail(String emailUrl, String randomPassword ) throws MessagingException;
}
