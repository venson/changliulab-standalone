package com.venson.changliulab.utils;

import com.venson.changliulab.entity.vo.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class ResUtils {
    public static ResponseEntity<Long> created(Long id){
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }


    public static <T> ResponseEntity<T> unAuthorized(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }
    public static ResponseEntity<ErrorMessage> internalError(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    public static ResponseEntity<ErrorMessage> internalError(String message){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMessage(message));
    }




}
