package com.venson.changliulab.entity.front.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CourseFrontTreeNodeVo {
    private String title;
    private Long id;

    private String description;
    private List<CourseFrontTreeNodeVo> children;

    public CourseFrontTreeNodeVo(Long id, String title){
        this.id = id;
        this.title = title;
    }
    public CourseFrontTreeNodeVo(Long id, String title,String desc){
        this.id = id;
        this.title = title;
        this.description = desc;
    }

}
