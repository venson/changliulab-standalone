package com.venson.changliulabstandalone.utils;

import com.venson.changliulabstandalone.entity.vo.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public abstract class ResUtils {
    public static ResponseEntity<Long> created(Long id){
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    public static <T> ResponseEntity<T> notFount(){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }
    public static <T> ResponseEntity<T> ok(T t){
        return ResponseEntity.status(HttpStatus.OK).body(t);
    }
    public static <T> ResponseEntity<T> ok(){
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
    public static <T> ResponseEntity<T> optional(T t){
        return t == null ? notFount(): ok(t);
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
