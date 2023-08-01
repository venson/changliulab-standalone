package com.venson.changliulab.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BasicListDTO<T> implements Serializable {
    private List<T> data;
    public static <T> BasicListDTO<T> of(List<T> data){
        return new BasicListDTO<>(data);
    }

}
