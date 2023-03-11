package com.venson.changliulabstandalone.entity.vo;

import com.venson.changliulabstandalone.entity.enums.MemberLevel;
import lombok.Data;

import java.io.Serializable;

@Data
public class MemberVo implements Serializable {

    private String name;

    private String intro;

    private String career;

    private MemberLevel level;

    private String avatar;

    private String title;

}
