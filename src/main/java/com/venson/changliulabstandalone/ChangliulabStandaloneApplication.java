package com.venson.changliulabstandalone;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.venson")
@MapperScan("com.venson.changliulabstandalone.mapper")
public class ChangliulabStandaloneApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChangliulabStandaloneApplication.class, args);
    }

}
