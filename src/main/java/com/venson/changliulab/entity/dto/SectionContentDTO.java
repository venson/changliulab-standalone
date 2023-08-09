package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.ReviewStatus;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SectionContentDTO implements Serializable {
    @Serial
    private static final long serialVersionUID=239829384L;
    private Long id;

    private Long courseId;

    private Long chapterId;

    private String title;

    private String videoLink;
    private Boolean isModified;

    private ReviewStatus review;

    private Integer sort;

    private String markdown;
    private String  htmlBrBase64;
}
