package com.venson.changliulab.entity.vo.admin;

import com.venson.changliulab.entity.enums.MemberLevel;

public record AdminMemberPageVo(Integer page,
                                Integer perPage,
//                                String type,
                                String name,
                                String[] filter,
                                MemberLevel level) {
}

