package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.MenuMeta;
import lombok.Data;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedHashSet;

@Data
@ToString
public class MenuDTO implements Serializable {
    @Serial
    private static final long serialVersionUID=23942934L;
    private String name;
    private String path;
    private String redirect;
    private String component;
    private Boolean hidden;
    private MenuMeta meta;
    private LinkedHashSet<MenuDTO> children = new LinkedHashSet<>();
}
