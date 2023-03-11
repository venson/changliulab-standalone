package com.venson.changliulabstandalone.entity.front.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ActivityFrontBriefDTO implements Serializable {
    @Serial
    private static final long serialVersionUID =2392938247L;
    private Long id;
    private String title;
    private String activityDate;
}
