package com.venson.changliulabstandalone.service.impl;

import com.venson.changliulabstandalone.service.MsmService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
public class MsmServiceImpl implements MsmService {

    @Autowired
    private JavaMailSender sender;
    @Autowired
    private SpringTemplateEngine springTemplateEngine;


    @Override
    public void sendCode(String emailUrl, String code, String codeName, String mailPurpose, Integer expire) throws MessagingException {
        MimeMessage mimeMessageMsg = sender.createMimeMessage();
        String subject = "Changliu Lab " + mailPurpose ;

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessageMsg,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
            HashMap<String,Object> values = new HashMap<>();
            values.put("codeName",codeName);
            values.put("code",code);
            values.put("mailPurpose",mailPurpose);
            values.put("expire", expire);
            Context context = new Context();
            context.setVariables(values);
            // use template to generate email context
            String htmlPage = springTemplateEngine.process("sendCode.html", context);
            helper.setTo(emailUrl);
            try {
                helper.setFrom("info@changliulab.com","Changliulab Admin");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            helper.setSubject(subject);
            helper.setText(htmlPage,true);
        sender.send(mimeMessageMsg);
    }

    @Override
    public void sendResetPasswordEmail(String emailUrl, String randomPassword) throws MessagingException {
        sendCode(emailUrl, randomPassword,"New Password", "Reset Password",0);
    }
}
