package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.enums.ReviewType;
import lombok.*;
import org.springframework.security.core.parameters.P;

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
    private ReviewType refType;
    private Long parentId;
    private LocalDateTime gmtCreate;
    private ReviewStatus review;
    private ReviewType type;
}
