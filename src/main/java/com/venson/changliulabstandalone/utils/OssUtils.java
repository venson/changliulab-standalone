package com.venson.changliulabstandalone.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.oss", ignoreUnknownFields = false)
public class OssUtils implements InitializingBean {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public void setEndpoint(final String endpoint){
        this.endpoint = endpoint;
    }
    public void setAccessKeyId(final String accessKeyId){
        this.accessKeyId=accessKeyId;
    }
    public void setAccessKeySecret(final String accessKeySecret){
        this.accessKeySecret= accessKeySecret;
    }
    public void setBucketName(final String bucketName){
        this.bucketName= bucketName;
    }


    public static String END_POINT;
    public static String ACCESS_KEY_ID;
    public static String ACCESS_KEY_SECRET;
    public static String BUCKET_NAME;

    @Override
    public void afterPropertiesSet() {
        END_POINT = endpoint;
        ACCESS_KEY_ID = accessKeyId;
        ACCESS_KEY_SECRET = accessKeySecret;
        BUCKET_NAME = bucketName;

    }
}
