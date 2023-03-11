package com.venson.changliulabstandalone.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.pojo.EduChapter;
import com.venson.changliulabstandalone.entity.dto.ChapterContentDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.vo.CourseTreeNodeVo;
import com.venson.changliulabstandalone.entity.front.dto.ChapterFrontDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-05-11
 */
public interface EduChapterService extends IService<EduChapter> {

    List<CourseTreeNodeVo> getCourseTreeByCourseId(Long courseId);


    void deleteChapterSectionByCourseId(Long courseId);


    void removeMarkChapterById(Long chapterId);

    void updateChapterById(Long chapterId, ChapterFrontDTO chapter);

    ChapterContentDTO getChapterDTOById(Long chapterId);

    Long addChapter(ChapterFrontDTO chapterFrontDTO);


    Map<Long, ReviewStatus> getChapterReviewStatusMap(boolean review);
}
