package com.venson.changliulabstandalone.utils;

public enum ResultCode {
    SUCCESS(20000,"Operation successful"),
    ERROR(20001,"Operation failed"),
    GATEWAY_ERROR(20005,"GateWay Error"),
    ILLEGAL_TOKEN(50008, "Illegal token"),
    OTHER_LOGIN(50012, "Account login on other device"),
    TOKEN_EXPIRE(50014, "Token Expire"),
    UNAUTHORIZED(50030, "UnAuthorized");


    private final int value;
    private final String desc;

    ResultCode(final Integer value, final String desc){
        this.value = value;
        this.desc = desc;
    }

    public int getValue(){return value;}

    public String getDesc(){return desc;}
}
