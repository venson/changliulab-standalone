package com.venson.changliulabstandalone.entity.enums;

public enum UserType {
    MEMBER(1,"Lab member"),
    USER(2,"normal user");

    final int value;
    final String desc;
    UserType(Integer value, String desc){
        this.value = value;
        this.desc = desc;
    }

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
