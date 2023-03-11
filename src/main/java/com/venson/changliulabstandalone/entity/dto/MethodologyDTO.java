package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.enums.LanguageEnum;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class MethodologyDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2378435671827346123L;

    private Long id;

    @NotBlank
    private String markdown;
    @NotBlank
    private String title;

    private String publishedMd;
    @NotBlank
    private String htmlBrBase64;
    private String publishedHtmlBrBase64;

    private LanguageEnum language;

    private Boolean isModified;

    private ReviewStatus review;

    private Boolean isPublished;

    private Boolean isPublic;
}
