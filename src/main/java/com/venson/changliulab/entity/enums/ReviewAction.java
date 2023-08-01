package com.venson.changliulab.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum ReviewAction implements IEnum<Integer> {

    PENDING(0),
    PASS(1),
    REJECT(2),

    REQUEST(3),
    PASS_ENTIRE(4),
    REJECT_ENTIRE(5),

    REQUEST_ENTIRE(6);

    private final int value;

    ReviewAction(int value){
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
