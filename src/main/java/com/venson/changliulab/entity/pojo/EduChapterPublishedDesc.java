package com.venson.changliulab.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;

import lombok.*;

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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EduChapterPublishedDesc implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String description;


}
