package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.enums.MemberLevel;
import lombok.*;

@Getter
@NoArgsConstructor
@Setter
@ToString
public class MemberQuery {

    private String name;

    private MemberLevel level;

    private String begin;

    private String end;
}
