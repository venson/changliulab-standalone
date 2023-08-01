package com.venson.changliulab.utils;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1123124421L;
    private Boolean success;
    private Integer code;
    private String message;
    private T data;
//    private Map<String,T> data = new HashMap<>();

    private Result(){
    }
    private Result(Boolean success, Integer code, String msg, T data){
        this.success = success;
        this.code = code;
        this.message = msg;
        this.data = data;
//        this.data.put("item",data);
    }


    public static <T> Result<T> code(Integer code,String msg){
        return new Result<>(false, code, msg,null);
    }
    public static <T> Result<T> success(T data,String msg){
        return new Result<>(true, ResultCode.SUCCESS.getValue(), msg, data);
    }
    public static  <T> Result<T> success(T data){
        return new Result<>(true, ResultCode.SUCCESS.getValue(), ResultCode.SUCCESS.getDesc(), data);
    }
    public static  <T> Result<T> success(){
        return new Result<>(true, ResultCode.SUCCESS.getValue(), ResultCode.SUCCESS.getDesc(), null);
    }
    public static  <T> Result<T> optional(T data){
        if(data ==null){
            return new Result<>(false, ResultCode.ERROR.getValue(), "Data not exist",null);
        }else {
            return new Result<>(true, ResultCode.SUCCESS.getValue(), ResultCode.SUCCESS.getDesc(), data);
        }

    }
    public static  <T> Result<T> error(String msg){
        return new Result<>(false, ResultCode.ERROR.getValue(), msg,null);
    }
    public static  <T> Result<T> error(){
        return new Result<>(false, ResultCode.ERROR.getValue(), ResultCode.ERROR.getDesc(), null);
    }
    public static  <T> Result<T> otherLogin(){
        return new Result<>(false, ResultCode.OTHER_LOGIN.getValue(), ResultCode.OTHER_LOGIN.getDesc(), null);
    }
    public static  <T> Result<T> tokenExpire(){
        return new Result<>(false, ResultCode.TOKEN_EXPIRE.getValue(), ResultCode.TOKEN_EXPIRE.getDesc(), null);
    }
    public static  <T> Result<T> unAuthorized(){
        return new Result<>(false, ResultCode.UNAUTHORIZED.getValue(), ResultCode.UNAUTHORIZED.getDesc(), null);
    }
    public static  <T> Result<T> illegalToken(){
        return new Result<>(false, ResultCode.ILLEGAL_TOKEN.getValue(), ResultCode.ILLEGAL_TOKEN.getDesc(), null);
    }

    public Result<T> message(String message){
        this.setMessage(message);
        return this;
    }
}
