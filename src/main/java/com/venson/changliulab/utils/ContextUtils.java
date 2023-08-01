package com.venson.changliulab.utils;


import com.venson.changliulab.entity.UserContextInfoBO;
import com.venson.changliulab.entity.enums.PermissionAction;
import com.venson.changliulab.exception.CustomizedException;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class ContextUtils {
    public static UserContextInfoBO getUserContext(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserContextInfoBO){
            return (UserContextInfoBO)principal;
        }
        return null;

    }

    public static String getUserName(){
        UserContextInfoBO userContext = getUserContext();
        if(userContext != null){
            return userContext.getUsername();
        }
        throw new CustomizedException("Please Login");
    }
    public static Long getUserId(){
        UserContextInfoBO userContext = getUserContext();
        if(userContext != null){
            return userContext.getId();
        }
        throw new CustomizedException("Please Login");
    }

    public static boolean hasAuthority(String permissionCate, PermissionAction action) {
        UserContextInfoBO userContext = getUserContext();
        if(userContext != null){
            return userContext.getPermissionValueList().contains(permissionCate + "." + action.toString());
        }
        return false;
    }
    public static boolean hasAuthority(String permission) {
        UserContextInfoBO userContext = getUserContext();
        if(userContext != null){
            return userContext.getPermissionValueList().contains(permission );
        }
        return false;
    }
}
