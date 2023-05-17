package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.EduReport;
import com.venson.changliulabstandalone.entity.EduReportPublished;
import com.venson.changliulabstandalone.entity.EduReportPublishedMd;
import com.venson.changliulabstandalone.entity.front.dto.ReportFrontDTO;
import com.venson.changliulabstandalone.mapper.EduReportPublishedMapper;
import com.venson.changliulabstandalone.service.EduReportPublishedMdService;
import com.venson.changliulabstandalone.service.EduReportPublishedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author venson
 * @since 2023-03-23
 */
@Service
public class EduReportPublishedServiceImp extends ServiceImpl<EduReportPublishedMapper, EduReportPublished> implements EduReportPublishedService {

    @Autowired
    private EduReportPublishedMdService reportPublishedMdService;

    @Override
    @Cacheable(value = FrontCacheConst.REPORT_PAGE_NAME, key = "#current +':'+#size")
    public PageResponse<EduReportPublished> getPageFrontReport(int current, int size) {
        LambdaQueryWrapper<EduReportPublished> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(EduReportPublished::getEnable, true);
        wrapper.select(EduReportPublished::getTitle, EduReportPublished::getId);
        Page<EduReportPublished> page = new Page<>(current, size);
        baseMapper.selectPage(page,wrapper);
        return PageUtil.toBean(page);
    }

    @Override
    public ReportFrontDTO getFrontReportById(int id) {
        EduReportPublished report = baseMapper.selectById(id);
        Assert.notNull(report,"Invalid Report");
        EduReportPublishedMd markdown = reportPublishedMdService.getById(id);
        Assert.notNull(markdown,"Invalid Report");
        ReportFrontDTO dto = new ReportFrontDTO();
        BeanUtils.copyProperties(report,dto);
        dto.setHtml_br_base64(markdown.getHtmlBrBase64());
        return dto;
    }
}
