package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.enums.LanguageEnum;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import lombok.Data;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
public class ResearchDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2378435671827346123L;

    private Long id;

    private String markdown;
    @NotBlank(message = "Title cannot be empty")
    private String title;

    private String publishedMd;
    private String htmlBrBase64;
    private String publishedHtmlBrBase64;

    private LanguageEnum language;

    private Boolean isModified;

    private ReviewStatus review;

    private Boolean enable;



}
