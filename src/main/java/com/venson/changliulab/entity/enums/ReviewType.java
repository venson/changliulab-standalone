package com.venson.changliulab.entity.enums;

import com.baomidou.mybatisplus.annotation.IEnum;

public enum ReviewType implements IEnum<Integer> {
    COURSE(1),
    CHAPTER(2),
    SECTION(3),
    ACTIVITY(4),
    RESEARCH(5),
    METHODOLOGY(6),
    REPORT(7),

    SYLLABUS(8);
//    COURSE_INFO(7,"course"),
//    CHAPTER_INFO(8,"course"),


    private final int value;

    ReviewType(int value) {
        this.value =value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }
}
