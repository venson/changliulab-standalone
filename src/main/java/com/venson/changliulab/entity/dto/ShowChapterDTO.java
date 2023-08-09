package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.inter.ReviewAble;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Builder
public class ShowChapterDTO implements ReviewAble, Serializable {
    @Serial
    private static final long serialVersionUID = 29393L;
    private String title;
    private String description;
    private Boolean isRemoveAfterReview;
    private ReviewStatus review;
    private Boolean isModified;

}
