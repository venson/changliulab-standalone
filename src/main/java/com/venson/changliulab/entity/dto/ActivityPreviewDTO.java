package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.ReviewStatus;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ActivityPreviewDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 2937345723645125634L;

    private Long id;

    private String title;

    private String activityDate;

    private String authorMemberId;

    private String authorMemberName;

    private ReviewStatus review;

    private Boolean isRemoveAfterReview;
    private String htmlBrBase64;

    private String publishedHtmlBrBase64;
}
