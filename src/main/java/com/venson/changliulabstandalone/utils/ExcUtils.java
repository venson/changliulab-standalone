package com.venson.changliulabstandalone.utils;

import com.venson.changliulabstandalone.exception.CustomizedException;

public abstract class ExcUtils {
    public static CustomizedException tokenExpired(){
        return new CustomizedException(ResultCode.TOKEN_EXPIRE.getValue(),ResultCode.TOKEN_EXPIRE.getDesc());
    }
    public static CustomizedException unAuthorized(){
        return new CustomizedException(ResultCode.UNAUTHORIZED.getValue(),ResultCode.UNAUTHORIZED.getDesc());
    }
}
