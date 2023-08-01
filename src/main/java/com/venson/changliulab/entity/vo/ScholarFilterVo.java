package com.venson.changliulab.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ScholarFilterVo implements Serializable {
    private String year;
    private String authors;
    private String title;
}
