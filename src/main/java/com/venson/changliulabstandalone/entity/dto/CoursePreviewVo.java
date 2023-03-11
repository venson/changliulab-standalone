package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CoursePreviewVo implements Serializable {
    @Serial
    private static final  long serialVersionUID = 22123323L;
    private Long id;
    private String title;
    private Integer totalHour;
    private String description;
    private Integer viewCount;
    private Boolean isModified;
    private ReviewStatus review;
    private Boolean isPublished;
    private String cover;
    private String avatar;
    private String memberId;
    private String memberName;
    private String memberTitle;
    private String l1Subject;
    private String l2Subject;
    private Boolean isRemoveAfterReview;
}
