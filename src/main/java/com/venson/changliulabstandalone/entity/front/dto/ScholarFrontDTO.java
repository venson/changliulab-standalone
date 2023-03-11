package com.venson.changliulabstandalone.entity.front.dto;

import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.pojo.EduScholar;
import com.venson.changliulabstandalone.entity.pojo.EduScholarCitation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScholarFrontDTO implements Serializable {
    @Serial
    private static final long serialVersionUID=2938423489L;
    private EduScholar scholar;
    private List<EduMember> members;
    private List<EduScholarCitation> citations;

}
