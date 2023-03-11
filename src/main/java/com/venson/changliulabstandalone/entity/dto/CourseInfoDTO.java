package com.venson.changliulabstandalone.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CourseInfoDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 234823651345L;

    private Long id;
    private Long memberId;
    private Long subjectId;
    private Long subjectParentId;
    @NotBlank
    private String title;
    private Boolean isPublic;
    private BigDecimal price;
    private Integer totalHour;
    private String cover;
    private String description;

}
