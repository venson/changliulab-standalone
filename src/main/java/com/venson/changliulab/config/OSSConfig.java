package com.venson.changliulab.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@ConfigurationProperties(prefix = "alibaba.cloud.oss")
@Configuration
public class OSSConfig {
    // yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
    String endpoint;
    // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
    String accessKey;
    String secretKey;

    public OSSConfig(@Value("${alibaba.cloud.access-key}") String accessKey,
                     @Value("${alibaba.cloud.secret-key}") String secretKey,
                     @Value("${alibaba.cloud.oss.endpoint}") String endpoint
                     ){
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.endpoint = endpoint;
    }

    // 创建OSSClient实例。
    @Bean
    public OSS ossClient(){
        return new OSSClientBuilder().build(endpoint,accessKey, secretKey);
    }

// 关闭OSSClient。
}
