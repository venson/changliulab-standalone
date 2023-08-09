package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.inter.ReviewAble;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShowResearchDTO implements ReviewAble, Serializable {
    @Serial
    private final static long  serialVersionUID = 347234L;

    private String title;
    private String html;
    private List<AvatarDTO> members;
}
