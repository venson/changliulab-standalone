package com.venson.changliulab.entity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class AdminReportDTO {
    private Long id;
    private String title;

    private Long authorMemberId;

    private String authorMemberName;

    private Boolean isRemoveAfterReview;

    private Long version;

    private String markdown;

    private Byte review;
}
