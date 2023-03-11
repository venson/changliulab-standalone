package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.enums.MemberLevel;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MemberQuery {

    private String name;

    private MemberLevel level;

    private String begin;

    private String end;
}
