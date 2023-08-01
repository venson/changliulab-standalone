package com.venson.changliulab.entity.front.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ActivityFrontDTO implements Serializable {
    @Serial
    private static final long serialVersionUID=29384987347L;
    private Long id;
    private String title;
    private String htmlBrBase64;
    private LocalDate activityDate;
    private String authorMemberName;
}
