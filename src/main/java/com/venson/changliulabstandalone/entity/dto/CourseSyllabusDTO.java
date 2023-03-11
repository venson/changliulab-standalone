package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class CourseSyllabusDTO implements Serializable {
    @Serial
    private static final long serialVersionUID=2348714765345681562L;

    private Long id;
    private String title;
    private String description;
    private ReviewStatus review;
    private List<CourseSyllabusDTO> children;

    private Boolean isRemoveAfterReview;

    public CourseSyllabusDTO(Long id,String title, String description,ReviewStatus review,Boolean isRemoveAfterReview ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.review = review;
        this.isRemoveAfterReview  = isRemoveAfterReview;
    }
}
