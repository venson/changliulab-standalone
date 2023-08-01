package com.venson.changliulab.entity.vo.admin;

import com.venson.changliulab.entity.enums.LanguageEnum;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.valid.UpdateGroup;
import jakarta.validation.constraints.NotBlank;

public record AdminMethodologyVo(long id, @NotBlank(groups = UpdateGroup.class) String markdown, @NotBlank String title,
                                 @NotBlank(groups = UpdateGroup.class) String htmlBrBase64, LanguageEnum language,
                                 ReviewStatus review, boolean isPublic) {
}
