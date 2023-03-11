package com.venson.changliulabstandalone.entity.statemachine;

import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateMachineContext implements Serializable {
    @Serial
    private static final long serialVersionUID = 237723744596723457L;

    private EduReview review;
    private ReviewApplyVo reviewVo;

}
