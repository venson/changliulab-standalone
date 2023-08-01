package com.venson.changliulab.entity.front.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ChapterFrontDTO implements Serializable {
    @Serial
    private static final long serialVersionUID=293841928357L;
    private Long id;
    private String title;
    private String description;
}
