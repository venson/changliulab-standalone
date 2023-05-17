package com.venson.changliulabstandalone.entity.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@Setter
@Getter
public class AdminPermissionTree implements Serializable {
    private String name;
    private Long id;
    private List<AdminPermissionTree> children;
    public AdminPermissionTree( String name , int id){
        this.name = name;
        this.id = (long) id;
        this.children = new LinkedList<>();
    }
    public AdminPermissionTree( String name , long id, List<AdminPermissionTree> children){
        this.name = name;
        this.id = id;
        this.children = children;
    }
}
