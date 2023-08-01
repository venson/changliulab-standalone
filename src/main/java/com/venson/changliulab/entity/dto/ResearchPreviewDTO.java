package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.LanguageEnum;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.front.dto.MemberFrontBriefDTO;
import com.venson.changliulab.entity.front.dto.MemberFrontDTO;
import lombok.*;

import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Setter
@Getter
@ToString
public class ResearchPreviewDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2378435671827346123L;

    private Long id;

    private String title;

//    private String publishedMd;
    private String htmlBrBase64;
//    private String publishedHtmlBrBase64;
    private List<AvatarDTO> members;

    private Boolean isPublished;

    private Boolean isModified;

    private ReviewStatus review;

    private Boolean enable;



}
