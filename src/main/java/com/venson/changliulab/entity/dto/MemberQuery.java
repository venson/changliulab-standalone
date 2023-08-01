package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.MemberLevel;
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
