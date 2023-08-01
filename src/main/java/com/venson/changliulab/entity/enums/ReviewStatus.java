package com.venson.changliulab.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;


public enum ReviewStatus implements IEnum<Integer> {
    NONE(1,8,7),
    APPLIED(2,10,8),
    REJECTED(3,9,10),
    FINISHED(4,7,9),
    PARTIAL(5,0,0),

    ;
    private final Integer value;
    private final Integer reviewOrder;
    private final Integer viewOrder;

    ReviewStatus(int value, Integer reviewOrder, Integer viewOrder) {
        this.value = value;
        this.reviewOrder = reviewOrder;
        this.viewOrder = viewOrder;

    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public Integer getReviewOrder() {
        return reviewOrder;
    }

    public Integer getViewOrder() {
        return viewOrder;
    }
}
