package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.dto.AvatarDTO;
import com.venson.changliulabstandalone.entity.pojo.EduReport;
import com.venson.changliulabstandalone.entity.pojo.EduReportMarkdown;
import com.venson.changliulabstandalone.entity.pojo.EduReportPublished;
import com.venson.changliulabstandalone.entity.pojo.EduReportPublishedMd;
import com.venson.changliulabstandalone.entity.dto.ShowReportDTO;
import com.venson.changliulabstandalone.entity.front.dto.ReportFrontDTO;
import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import com.venson.changliulabstandalone.mapper.EduReportPublishedMapper;
import com.venson.changliulabstandalone.service.EduReportPublishedMdService;
import com.venson.changliulabstandalone.service.EduReportPublishedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.service.admin.EduMemberService;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.PageUtil;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class EduReportPublishedServiceImp extends ServiceImpl<EduReportPublishedMapper, EduReportPublished> implements EduReportPublishedService {

    private final EduReportPublishedMdService reportPublishedMdService;
    private final EduMemberService memberService;

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

    @Override
    public ReviewAble getReviewById(Long refId) {

        EduReportPublished report= baseMapper.selectById(refId);
        AvatarDTO avatar = memberService.getMemberAvatarById(refId);
        EduReportPublishedMd markdown = reportPublishedMdService.getById(refId);
        return ShowReportDTO.builder().title(report.getTitle())
                .html(markdown.getHtmlBrBase64())
                .videoLink(report.getVideoLink())
                .speaker(avatar)
                .build();
    }
}
