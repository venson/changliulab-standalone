package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.inter.ReviewAble;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class ShowSyllabusDTO implements ReviewAble, Serializable {
    @Serial
    private static final long serialVersionUID = 123423L;
    private Long id;
    private String title;
    private ReviewStatus review;
    private Boolean isRemoveAfterReview;
    private Boolean isModified;
//    private String html;
//    private CourseInfoDTO info;
//    private Boolean isPublic;
    private List<CourseSyllabusDTO> syllabus;


}
