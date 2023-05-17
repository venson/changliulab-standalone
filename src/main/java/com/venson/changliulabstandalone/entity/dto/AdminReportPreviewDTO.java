package com.venson.changliulabstandalone.entity.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdminReportPreviewDTO {
    private Long id;
    private String html_br_base64;
    private String published_html_br_base64;
    private String title;
    private Long authorMemberId;

    private String authorMemberName;
    private Boolean isRemoveAfterReview;
}
