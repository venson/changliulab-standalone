package com.venson.changliulab.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import com.venson.changliulab.entity.enums.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 课程
 * </p>
 *
 * @author venson
 * @since 2022-07-12
 */
@Getter
@Setter
@TableName("edu_course")
public class EduCourse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 课程ID
     */

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 课程讲师ID
     */
    private Long memberId;
    /**
     * 课程讲师ID
     */
    private Long memberName;

    /**
     * 课程专业ID
     */
    private Long subjectId;

    /**
     * 课程专业父级ID
     */
    private Long subjectParentId;

    /**
     * 课程标题
     */
    private String title;

    /**
     * 总课时
     */
    private Integer totalHour;

    /**
     * 课程封面图片路径
     */
    private String cover;

    /**
     * 乐观锁
     */
    @Version
    private Long version;

    /**
     * 逻辑删除 1（true）已删除， 0（false）未删除
     */
    @TableLogic
    private Integer isDeleted;

    /**
     * 课程状态 0未发布  1已发布
     */
    private Boolean isPublished;

    /**
     *  1（true）modified since publish， 0（false）not modified since publish
     */
    private Boolean isModified;

    /**
     * 0- , 1 request for review , 2 review request rejected
     */
    private ReviewStatus review;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime gmtModified;

    /**
     * 课程类别，1 免费公开，0免费注册
     */
    private Boolean isPublic;

    private Boolean isRemoveAfterReview;

    private Long viewCount;


}
