package com.venson.changliulab.entity.pojo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("edu_scholar_citation")
public class EduScholarCitation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long scholarId;

    private Integer year;

    private Integer citations;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    @Version
    private Long version;


}
