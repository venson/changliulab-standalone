package com.venson.changliulabstandalone.service.admin;

import com.venson.changliulabstandalone.entity.pojo.EduChapterPublished;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.front.dto.ChapterFrontDTO;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author venson
 * @since 2022-07-12
 */
public interface EduChapterPublishedService extends IService<EduChapterPublished> {

//    void passReview(Long id);

    List<ChapterFrontDTO> getFrontChaptersByCourseId(Long id);
}
