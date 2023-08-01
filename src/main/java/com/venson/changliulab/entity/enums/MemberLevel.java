package com.venson.changliulab.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum MemberLevel implements IEnum<Integer> {
    INTERN(1),
    FORMER_MEMBER(2),
    CURRENT_MEMBER(3),
    TECH(4),
    PI(5);

    private final Integer value;

    MemberLevel(Integer value){
        this.value = value;
    }
    @Override
    public Integer getValue() {
        return value;
    }
}
