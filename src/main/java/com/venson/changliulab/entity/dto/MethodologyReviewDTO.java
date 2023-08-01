package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.pojo.EduMethodology;
import com.venson.changliulab.entity.pojo.EduReview;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MethodologyReviewDTO extends EduMethodology {
    private List<EduReview> reviewList;
}
