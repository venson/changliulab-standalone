package com.venson.changliulabstandalone.entity.front.dto;

import com.venson.changliulabstandalone.entity.pojo.EduMember;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
public class ResearchFrontDTO implements Serializable {
    @Serial
    private static final long serialVersionUID=293284382L;
    private Long id;

    private String publishedHtmlBrBase64;
    private String title;

    private List<EduMember> members;

}
