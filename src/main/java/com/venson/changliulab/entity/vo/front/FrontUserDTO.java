package com.venson.changliulab.entity.vo.front;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
public class FrontUserDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 9384592345L;

    private Long id;
    private String email;
    private String username;


}
