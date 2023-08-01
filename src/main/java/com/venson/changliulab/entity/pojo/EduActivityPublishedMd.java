package com.venson.changliulab.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
@Getter
@Setter
@TableName("edu_activity_published_md")
public class EduActivityPublishedMd implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String markdown;

    private String htmlBrBase64;


}
