package com.venson.changliulabstandalone.entity.vo.admin;

import com.venson.changliulabstandalone.entity.enums.MemberLevel;

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
