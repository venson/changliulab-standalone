package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.inter.ReviewAble;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
public class ShowSectionDTO implements ReviewAble, Serializable {
    @Serial
    private static final long serialVersionUID = 29393L;
    private String title;
    private String html;
    private String videoLink;
    private Boolean isRemoveAfterReview;
    private ReviewStatus review;

}
