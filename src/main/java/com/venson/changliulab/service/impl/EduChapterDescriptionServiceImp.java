package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.entity.pojo.EduChapterDescription;
import com.venson.changliulab.mapper.EduChapterDescriptionMapper;
import com.venson.changliulab.service.admin.EduChapterDescriptionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-07-12
 */
@Service
public class EduChapterDescriptionServiceImp extends ServiceImpl<EduChapterDescriptionMapper, EduChapterDescription> implements EduChapterDescriptionService {
//    private final EduChapterPublishedDescService publishedMdService;
//
//    public EduChapterDescriptionServiceImp(EduChapterPublishedDescService publishedMdService) {
//        this.publishedMdService = publishedMdService;
//    }

//    @Override
//    public void saveOrUpdateToPublishedDesc(List<Long> updateIdList) {
//        List<EduChapterDescription> markdownList = baseMapper.selectBatchIds(updateIdList);
//        EduChapterPublishedDesc publishedMd = new EduChapterPublishedDesc();
//        markdownList.parallelStream().forEach(o->{
//            BeanUtils.copyProperties(o,publishedMd);
//            publishedMdService.saveOrUpdate(publishedMd);
//        });
//    }
}
