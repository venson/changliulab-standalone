package com.venson.changliulabstandalone.entity.pojo;

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
 * @since 2022-07-12
 */
@Getter
@Setter
@TableName("edu_chapter_published_desc")
public class EduChapterPublishedDesc implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String description;


}
