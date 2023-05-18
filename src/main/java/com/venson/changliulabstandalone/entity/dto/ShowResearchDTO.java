package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class ShowResearchDTO implements ReviewAble, Serializable {
    @Serial
    private final static long  serialVersionUID = 347234L;

    private String title;
    private String html;
    private List<EduMember> members;
}
