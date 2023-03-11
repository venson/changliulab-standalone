package com.venson.changliulabstandalone.entity.front.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class IndexFrontDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2389234L;
    private List<MemberFrontBriefDTO> member;
    private List<CourseFrontBriefDTO> course;
    private List<ActivityFrontBriefDTO> activity;
}
