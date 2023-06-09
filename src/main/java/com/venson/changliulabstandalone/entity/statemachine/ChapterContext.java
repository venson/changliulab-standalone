package com.venson.changliulabstandalone.entity.statemachine;

import com.venson.changliulabstandalone.entity.pojo.EduChapter;
import com.venson.changliulabstandalone.entity.dto.ReviewApplyVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ChapterContext implements Serializable {
    @Serial
    private static final long serialVersionUID = 774596723457L;

    private EduChapter chapter;
    private ReviewApplyVo reviewVo;

}
