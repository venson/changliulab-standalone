package com.venson.changliulabstandalone.entity.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Data
public class BannerDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2341896235673475L;

    private Long id;

    @NotBlank
    private String title;

    private String imageUrl;

    private String imageOutlinkUrl;

    private Integer sort;


    private Boolean enable;
}
