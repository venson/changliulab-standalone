package com.venson.changliulab.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum LanguageEnum implements IEnum<Integer> {
    CHINESE(1),
    ENGLISH(2),
    ;

    private final int value;

    LanguageEnum(int value) {
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return value;
    }
}
