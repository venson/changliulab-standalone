package com.venson.changliulab.entity.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Builder
public class AvatarDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2939423874L;
    private Long id;
    private String name;
    private String avatar;
}
