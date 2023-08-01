package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.inter.ReviewAble;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Builder
public class ShowMethodologyDTO implements ReviewAble, Serializable {
    @Serial
    private final static long  serialVersionUID = 347234L;

    private String title;
    private String html;
    private Boolean isPublic;
}
