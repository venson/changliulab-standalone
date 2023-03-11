package com.venson.changliulabstandalone.entity.front.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CourseFrontBriefDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 8957834L;

    private Long id;

    private String title;

    private String cover;

    private Long viewCount;

    private Boolean isPublic;
}
