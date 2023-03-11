package com.venson.changliulabstandalone.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author venson
 * @since 2022-06-20
 */
@Getter
@Setter
@NoArgsConstructor
@TableName("edu_member_scholar")
public class EduMemberScholar implements Serializable {
    public EduMemberScholar(Long memberId, String name,Long scholarId){
        this.memberId =memberId;
        this.name = name;
        this.scholarId= scholarId;
    }

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String name;

    private Long scholarId;

    private Long memberId;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;


}
