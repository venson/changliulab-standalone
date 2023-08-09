package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.pojo.EduReviewMsg;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class ReviewTargetDTO<T> {
    private Long id;
    private String title;
    private T reviewed;
    private T applied;
    private ReviewStatus status;

    private List<EduReviewMsg> message;
}
