package com.venson.changliulab.mapper;

import com.venson.changliulab.entity.pojo.EduChapterPublished;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.venson.changliulab.entity.front.dto.ChapterFrontDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author venson
 * @since 2022-07-12
 */
@Mapper
public interface EduChapterPublishedMapper extends BaseMapper<EduChapterPublished> {

    List<ChapterFrontDTO> getFrontChaptersByCourseId(Long id);
}
