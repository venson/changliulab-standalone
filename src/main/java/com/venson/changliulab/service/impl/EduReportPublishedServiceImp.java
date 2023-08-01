package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.dto.AvatarDTO;
import com.venson.changliulab.entity.pojo.EduReportPublished;
import com.venson.changliulab.entity.pojo.EduReportPublishedMd;
import com.venson.changliulab.entity.dto.ShowReportDTO;
import com.venson.changliulab.entity.front.dto.ReportFrontDTO;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.mapper.EduReportPublishedMapper;
import com.venson.changliulab.service.EduReportPublishedMdService;
import com.venson.changliulab.service.EduReportPublishedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.service.admin.EduMemberService;
import com.venson.changliulab.utils.Assert;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
        if(report ==null) return null;
        AvatarDTO avatar = memberService.getMemberAvatarById(refId);
        EduReportPublishedMd markdown = reportPublishedMdService.getById(refId);
        return ShowReportDTO.builder().title(report.getTitle())
                .html(markdown.getHtmlBrBase64())
                .videoLink(report.getVideoLink())
                .speaker(avatar)
                .build();
    }
}
