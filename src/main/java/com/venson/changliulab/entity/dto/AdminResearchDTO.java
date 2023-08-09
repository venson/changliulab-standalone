package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.enums.LanguageEnum;
import com.venson.changliulab.entity.enums.ReviewStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AdminResearchDTO {

    private Long id;

    private String markdown;
    @NotBlank(message = "Title cannot be empty")
    private String title;

    private String htmlBrBase64;

    private LanguageEnum language;
    private ReviewStatus review;
    private List<Long> memberIds;
    private Boolean isModified;
//    private List<BasicMemberVo> members;
    private List<AvatarDTO> members;
}

