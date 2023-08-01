package com.venson.changliulab.entity.front.dto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class MethodologyFrontDTO implements Serializable {

    @Serial
    private static final long serialVersionUID= 37426956235L;
    private Long id;
    private String title;

    private String publishedHtmlBrBase64;
    private Boolean isPublic;

}
