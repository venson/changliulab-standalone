package com.venson.changliulabstandalone.exception;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomizedException extends RuntimeException {
    private int code;
    private String msg;

    public CustomizedException(String msg){
        this.code = 20001;
        this.msg = msg;
    }
    @Override
    public String getMessage() {
        return msg;
    }

    public Integer getCode(){return code;}
}
