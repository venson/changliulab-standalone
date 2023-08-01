package com.venson.changliulab.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuMeta implements Serializable {
    @Serial
    private final static long serialVersionUID=92384928L;
    private String title;
    private Boolean hidden;
    private String icon;
}
