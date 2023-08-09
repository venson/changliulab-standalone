package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.ReviewStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Builder
public class MethodologyPreviewDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2378435671827346123L;

    private Long id;

    private String title;

    private String htmlBrBase64;


    private Boolean isModified;

    private ReviewStatus review;

    private Boolean isPublished;

    private Boolean isPublic;
}
