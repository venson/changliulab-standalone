package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.enums.ReviewType;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class CourseSyllabusDTO implements Serializable {
    @Serial
    private static final long serialVersionUID=2348714765345681562L;

    private Long id;
    private String title;
    private String description;
    private ReviewStatus review;
    private List<CourseSyllabusDTO> children;
    private  Boolean isModified;
    private Boolean isRemoveAfterReview;
    private ReviewType type;

}
