package com.venson.changliulabstandalone.entity.vo.admin;

import com.venson.changliulabstandalone.entity.enums.LanguageEnum;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.valid.UpdateGroup;
import jakarta.validation.constraints.NotBlank;

public record AdminMethodologyVo(long id, @NotBlank(groups = UpdateGroup.class) String markdown, @NotBlank String title,
                                 @NotBlank(groups = UpdateGroup.class) String htmlBrBase64, LanguageEnum language,
                                 ReviewStatus review, boolean isPublic) {
}
