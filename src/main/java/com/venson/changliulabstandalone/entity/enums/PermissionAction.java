package com.venson.changliulabstandalone.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum PermissionAction implements IEnum<Integer> {
    INVALID(0),
    CREATE(1),
    EDIT(2),
    REMOVE(3),
    READ(4),
    REVIEW_REQUEST(11),
    REVIEW_PASS(12),
    REVIEW_REJECT(13),
    REVIEW_READ(14),

    ENABLE(21),
    PUBLIC(22),
    ;

    PermissionAction(int value){this.value = value;}
    @Override
    public Integer getValue() {
        return value;
    }

    private int value;
}
