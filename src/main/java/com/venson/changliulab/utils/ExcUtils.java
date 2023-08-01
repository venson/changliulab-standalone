package com.venson.changliulab.utils;

import com.venson.changliulab.exception.CustomizedException;

public abstract class ExcUtils {
    public static CustomizedException tokenExpired(){
        return new CustomizedException(ResultCode.TOKEN_EXPIRE.getValue(),ResultCode.TOKEN_EXPIRE.getDesc());
    }
    public static CustomizedException unAuthorized(){
        return new CustomizedException(ResultCode.UNAUTHORIZED.getValue(),ResultCode.UNAUTHORIZED.getDesc());
    }
}
