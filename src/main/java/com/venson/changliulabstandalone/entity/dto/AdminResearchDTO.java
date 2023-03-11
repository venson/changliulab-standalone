package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.enums.LanguageEnum;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.vo.BasicMemberVo;
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
//    private List<Long> memberIds;
    private List<BasicMemberVo> members;
}
