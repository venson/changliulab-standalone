package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.enums.ReviewType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReviewBasicDTO implements Serializable {
    private String title;
    private String subTitle;
    private Long id;
    private Long refId;
    private LocalDateTime gmtCreate;
    private ReviewStatus review;
    private ReviewType type;
}
