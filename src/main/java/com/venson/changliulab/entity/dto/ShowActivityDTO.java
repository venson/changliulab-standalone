package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.inter.ReviewAble;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowActivityDTO implements ReviewAble, Serializable {
    @Serial
    private static final long serialVersionUID = 2939273L;
    private String title;
    private String html;
    private LocalDate date;

}
