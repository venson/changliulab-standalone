package com.venson.changliulab.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 21342134L;
    private Long id;
    private String title;
}
