package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.ReviewStatus;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CoursePageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID=341288834L;
    private Long id;
    private String title;
    private Boolean isPublic;
    private Integer totalHour;
    private ReviewStatus infoReview;
    private ReviewStatus chapterReview;
    private ReviewStatus sectionReview;
    private Integer viewCount;
    private Boolean isPublished;
    private Boolean isModified;
    private Boolean isRemoveAfterReview;
}
