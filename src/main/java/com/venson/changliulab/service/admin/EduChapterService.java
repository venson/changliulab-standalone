package com.venson.changliulab.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.pojo.EduChapter;
import com.venson.changliulab.entity.dto.ChapterContentDTO;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.vo.CourseTreeNodeVo;
import com.venson.changliulab.entity.front.dto.ChapterFrontDTO;
import com.venson.changliulab.entity.vo.admin.ReviewSMContext;

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

    boolean existsByIds(List<Long> ids);

    void updateReviewStatusByIds(List<Long> ids, ReviewStatus reviewStatus);

    Long getCourseId(ReviewSMContext context);

    void publishReviewedChapters(List<Long> ids);

//    Collection<? extends ReviewBasicDTO> getInfoByReviews(List<EduReview> values);
}
