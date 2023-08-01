package com.venson.changliulab.entity.vo.admin;

import com.venson.changliulab.entity.enums.MemberLevel;

public record AdminMemberVo(

        String name,

        String intro,

        String career,

        MemberLevel level,

        String avatar,

        String title,

        Integer sort
) {
}
