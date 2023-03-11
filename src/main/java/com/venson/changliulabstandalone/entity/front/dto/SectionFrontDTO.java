package com.venson.changliulabstandalone.entity.front.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SectionFrontDTO implements Serializable {
    @Serial
    private static final long serialVersionUID=12318382348L;
    private String title;
    private String htmlBrBase64;
    private String videoLink;
    private Boolean isPublic;
}
