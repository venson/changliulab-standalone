package com.venson.changliulab.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum ResearchMemberStatus implements IEnum<Integer>{
    PENDING(0),
    APPROVED(1),
    ARCHIVED(2),
    ;
    final int value;

    ResearchMemberStatus(int value){
        this.value = value;
    }
    @Override
    public Integer getValue() {
        return value;
    }
}
