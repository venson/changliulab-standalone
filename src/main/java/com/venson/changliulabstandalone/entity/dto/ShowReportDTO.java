package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Builder
public class ShowReportDTO implements ReviewAble, Serializable {
    @Serial
    private static final long serialVersionUID = 293L;
    private String title;
    private AvatarDTO speaker;
    private String html;
    private String videoLink;

}
