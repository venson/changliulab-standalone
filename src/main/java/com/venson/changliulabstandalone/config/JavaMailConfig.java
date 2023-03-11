package com.venson.changliulabstandalone.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSenderImpl;

//@Configuration
@Slf4j
@ConfigurationProperties(prefix = "spring.mail")
public class JavaMailConfig {

    @Value("${spring.mail.port}")
    private Integer port;

    @Value("${spring.mail.host}")
    private String host;

    private String username;

    @Bean
    public JavaMailSenderImpl javaMailSender(){
        log.info(port.toString());
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setPort(port);
        mailSender.setHost(host);
        return mailSender;
    }
}
