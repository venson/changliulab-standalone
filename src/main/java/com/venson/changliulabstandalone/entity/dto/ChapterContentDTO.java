package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ChapterContentDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 23943478234976523L;

    private Long id;

    private Long courseId;

    private String title;

    private Boolean isPublished;

    private Boolean isModified;

    private ReviewStatus review;

    private Integer sort;

    private Boolean isRemoveAfterReview;

    private String description;

}
