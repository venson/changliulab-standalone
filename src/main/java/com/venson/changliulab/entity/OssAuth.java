package com.venson.changliulab.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class OssAuth implements Serializable {
    @Serial
    private static final long serialVersionUID = 239897348753L;
    private String accessId;
    private String policy;
    private String signature;
    private String dir;
    private String host;
    private String expire;

}
