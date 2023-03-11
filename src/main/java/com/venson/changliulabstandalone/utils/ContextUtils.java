package com.venson.changliulabstandalone.utils;


import com.venson.changliulabstandalone.entity.UserContextInfoBO;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class ContextUtils {
    public static UserContextInfoBO getUserContext(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserContextInfoBO){
            return (UserContextInfoBO)principal;
        }
        return null;

    }
}
