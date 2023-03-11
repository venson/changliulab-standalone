package com.venson.changliulabstandalone.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TransactionTemplateConfig {
//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionTemplate transactionTemplate(){
        return new TransactionTemplate(transactionManager);
    }
}
