package com.venson.changliulabstandalone.entity.front.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
public class MemberFrontBriefDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 239848587348L;
    private Long id;
    private String name;
    private String avatar;
    private String title;
    private String intro;
    private String career;
}
