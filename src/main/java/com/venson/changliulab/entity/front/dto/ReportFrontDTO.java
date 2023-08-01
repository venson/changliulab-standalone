package com.venson.changliulab.entity.front.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReportFrontDTO {
    private Long id;
    private String html_br_base64;
    private Long author_member_id;
    private String author_member_name;
}
