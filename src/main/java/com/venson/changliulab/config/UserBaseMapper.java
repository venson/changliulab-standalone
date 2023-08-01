package com.venson.changliulab.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;
import java.util.Optional;

public interface UserBaseMapper<T> extends BaseMapper<T> {

    default T selectByIdOp(Serializable id){
        return Optional.of(selectById(id)).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }

}
