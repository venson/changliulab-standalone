package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.pojo.EduMethodology;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MethodologyReviewDTO extends EduMethodology {
    private List<EduReview> reviewList;
}
