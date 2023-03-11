package com.venson.changliulabstandalone.entity.front.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class CourseSyllabusFrontDTO implements Serializable {
    @Serial
    private static final long serialVersionUID=234871476273461562L;

    private Long id;
    private String title;
    private String description;
    private List<CourseSyllabusFrontDTO> children;


    public CourseSyllabusFrontDTO(Long id,String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
}
