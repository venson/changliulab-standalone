package com.venson.changliulabstandalone.service.admin;

import com.venson.changliulabstandalone.entity.pojo.EduSectionMarkdown;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2022-07-12
 */
public interface EduSectionMarkdownService extends IService<EduSectionMarkdown> {

    void saveOrUpdateToPublishedMd(List<Long> updateIdList);
}
