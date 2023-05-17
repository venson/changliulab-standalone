package com.venson.changliulabstandalone.entity.vo.admin;

import com.venson.changliulabstandalone.entity.enums.MemberLevel;

public record AdminMemberPageVo(Integer page,
                                Integer perPage,
//                                String type,
                                String name,
                                String[] filter,
                                MemberLevel level) {
}

