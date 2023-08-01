package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.pojo.EduReviewMsg;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.enums.ReviewType;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ReviewDTO<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 7936434L;
    private Long id;
    private ReviewStatus status;
    private ReviewType refType;
    private Long refId;
    private T reviewed;
    private T applied;
    private LocalDateTime gmtCreate;
    private LocalDateTime gmtModified;
    private List<EduReviewMsg > messages;

}
