package com.venson.changliulab.service.impl;

import com.venson.changliulab.entity.pojo.EduSectionMarkdown;
import com.venson.changliulab.entity.pojo.EduSectionPublishedMd;
import com.venson.changliulab.mapper.EduSectionMarkdownMapper;
import com.venson.changliulab.service.admin.EduSectionMarkdownService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.service.admin.EduSectionPublishedMdService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-07-12
 */
@Service
public class EduSectionMarkdownServiceImp extends ServiceImpl<EduSectionMarkdownMapper, EduSectionMarkdown> implements EduSectionMarkdownService {
    private final EduSectionPublishedMdService publishedMdService;

    public EduSectionMarkdownServiceImp(EduSectionPublishedMdService publishedMdService) {
        this.publishedMdService = publishedMdService;
    }

    @Override
    public void saveOrUpdateToPublishedMd(List<Long> updateIdList) {
        List<EduSectionMarkdown> markdownList = baseMapper.selectBatchIds(updateIdList);
        EduSectionPublishedMd publishedMd = new EduSectionPublishedMd();
        markdownList.parallelStream().forEach(o->{
            BeanUtils.copyProperties(o,publishedMd);
            publishedMdService.saveOrUpdate(publishedMd);
        });
    }
}
