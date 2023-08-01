package com.venson.changliulab.entity.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SectionPreviewDTO implements Serializable {
    @Serial
    private static final long serialVersionUID=2937393423764L;
    private Long id;
    private String htmlBrBase64;
    private String videoLink;
}
