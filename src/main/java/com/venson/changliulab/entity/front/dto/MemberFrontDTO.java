package com.venson.changliulab.entity.front.dto;

import com.venson.changliulab.entity.pojo.EduCoursePublished;
import com.venson.changliulab.entity.pojo.EduScholar;
import com.venson.changliulab.utils.PageResponse;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberFrontDTO implements Serializable {
    @Serial
    private static final long serialVersionUID= 2899238423482437L;

    private  MemberFrontBriefDTO member;

    private PageResponse<EduCoursePublished> courses;
    private PageResponse<EduScholar> scholars;
    private List<ResearchFrontDTO> researches;
}
