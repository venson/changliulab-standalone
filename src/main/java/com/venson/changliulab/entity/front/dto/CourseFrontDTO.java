package com.venson.changliulab.entity.front.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class CourseFrontDTO extends CourseFrontBriefDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 895723723834L;

    private Long memberId;

    private Long subjectId;

    private Long subjectParentId;

    private Integer totalHour;
}
