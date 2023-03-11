package com.venson.changliulabstandalone.entity.statemachine;

import com.venson.changliulabstandalone.entity.pojo.EduSection;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class SectionContext implements Serializable {
    @Serial
    private static final long serialVersionUID = 94587456436745967L;

    private EduSection section;
    private ReviewApplyVo reviewVo;

}
