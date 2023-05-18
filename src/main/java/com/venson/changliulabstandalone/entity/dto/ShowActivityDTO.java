package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
public class ShowActivityDTO implements ReviewAble, Serializable {
    @Serial
    private static final long serialVersionUID = 2939273L;
    private String title;
    private String html;
    private LocalDate date;

}
