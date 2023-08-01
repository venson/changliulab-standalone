package com.venson.changliulab.service.impl;

import com.venson.changliulab.entity.pojo.EduChapterPublished;
import com.venson.changliulab.entity.front.dto.ChapterFrontDTO;
import com.venson.changliulab.mapper.EduChapterPublishedMapper;
import com.venson.changliulab.service.admin.EduChapterPublishedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-07-12
 */
@Service
public class EduChapterPublishedServiceImp extends ServiceImpl<EduChapterPublishedMapper, EduChapterPublished> implements EduChapterPublishedService {

//    @Override
//    public void passReview(Long id) {
//        EduChapterPublished chapter = baseMapper.selectById(id);
//    }

    @Override
    @Cacheable(value = "chapter",key = "'front_courseId:' + #id")
    public List<ChapterFrontDTO> getFrontChaptersByCourseId(Long id) {
        return baseMapper.getFrontChaptersByCourseId(id);
    }
}
