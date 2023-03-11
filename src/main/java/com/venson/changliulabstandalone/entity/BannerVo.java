package com.venson.changliulabstandalone.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class BannerVo implements Serializable {
    @Serial
    private static final long serialVersionUID = 23418289365673475L;

    private Long id;

    @NotBlank
    private String title;

    private String imageUrl;

    private String imageOutlinkUrl;
}

