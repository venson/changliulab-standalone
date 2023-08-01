package com.venson.changliulab.entity.subject;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class SubjectTreeNode implements Serializable {
    private Long id;
    private String title;
    /**
     * useful when update subject list, the subject will be created
     * if the corresponding subjectTreeNode  addNew was set to true
     */
    private Integer level;
    private Boolean addNew;
    /**
     * useful when update subject list, the subject will be removed
     * if the corresponding subjectTreeNode remove was set to true
     */
    private Boolean remove;
    private Boolean update;
//    private List<SubjectTreeNode> children= new ArrayList<>();
    private List<SubjectTreeNode> children;


    public SubjectTreeNode(Long id, String title,Integer level) {
        this.id = id;
        this.title = title;
        this.level = level;
    }
}
