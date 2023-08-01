package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduSubject;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
public class ShowCourseDTO implements ReviewAble, Serializable {
    @Serial
    private static final long serialVersionUID = 29393L;
    private Long id;
    private String title;
//    private String html;
    private AvatarDTO author;
    private SubjectDTO subject;
    private SubjectDTO parentSubject;
    private Integer totalHour;
    private Boolean isPublic;
    private String cover;

}
