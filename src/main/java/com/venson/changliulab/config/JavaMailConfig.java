package com.venson.changliulab.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

//@Configuration
@Slf4j
@ConfigurationProperties(prefix = "spring.mail")
@Configuration
public class JavaMailConfig {

    private Integer port;

    private String host;

    private String username;

    private String password;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Bean
    public JavaMailSenderImpl javaMailSender(){
        log.info(port.toString());
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setPort(port);
        mailSender.setHost(host);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        return mailSender;
    }
}
