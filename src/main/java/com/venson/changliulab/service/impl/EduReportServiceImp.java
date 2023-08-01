package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulab.entity.pojo.*;
import com.venson.changliulab.entity.dto.*;
import com.venson.changliulab.entity.enums.PageType;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.mapper.EduReportMapper;
import com.venson.changliulab.service.EduReportMarkdownService;
import com.venson.changliulab.service.EduReportPublishedMdService;
import com.venson.changliulab.service.EduReportPublishedService;
import com.venson.changliulab.service.EduReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.service.admin.EduMemberService;
import com.venson.changliulab.service.admin.ReviewableService;
import com.venson.changliulab.utils.Assert;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
public class EduReportServiceImp extends ServiceImpl<EduReportMapper, EduReport> implements EduReportService, ReviewableService {
    private final EduReportMarkdownService reportMarkdownService;
    private final EduReportPublishedMdService reportPublishedMdService;
    private final EduReportPublishedService publishedService;
    private  final EduMemberService memberService;

    @Override
    public PageResponse<EduReport> getPageReport(int current, int size, PageType type) {
        LambdaQueryWrapper<EduReport> wrapper = Wrappers.lambdaQuery();
        Page<EduReport> page = new Page<>(current,size);
        if(PageType.REVIEW.equals(type)){
            wrapper.eq(EduReport::getReview, ReviewStatus.APPLIED);
        }
        wrapper.select(EduReport::getId, EduReport::getReview,
                EduReport::getEnable,EduReport::getIsPublished,
                EduReport::getIsRemoveAfterReview, EduReport::getVersion,
                EduReport::getTitle);
        return PageUtil.toBean(baseMapper.selectPage(page,wrapper));
    }

    @Override
    public AdminReportDTO getReportById(long id) {
        EduReport eduReport = baseMapper.selectById(id);
        EduReportMarkdown markdown = reportMarkdownService.getById(id);

        AdminReportDTO dto = new AdminReportDTO();
        BeanUtils.copyProperties(eduReport,dto);
        dto.setMarkdown(markdown.getMarkdown());
        return dto;
    }

    @Override
    public Long addReport(AdminReportDTO dto) {
        Assert.isTrue(checkTitleUsable(dto.getTitle(),null),"Duplicated title"  );
        EduReport eduReport = new EduReport();
        BeanUtils.copyProperties(dto,eduReport);
        baseMapper.insert(eduReport);
        EduReportMarkdown markdown = new EduReportMarkdown();
        markdown.setId(eduReport.getId());
        reportMarkdownService.save(markdown);

        return eduReport.getId();
    }
    @Override
    public long updateReport(Long id, AdminReportDTO dto) {
        Assert.isTrue(id.equals(dto.getId()),"Invalid Data" );
        Assert.isTrue(checkTitleUsable(dto.getTitle(),id),"Duplicated title"  );
        EduReport eduReport = baseMapper.selectById(id);
        EduReportMarkdown markdown = reportMarkdownService.getById(id);

        BeanUtils.copyProperties(dto,eduReport);
        baseMapper.updateById(eduReport);
        markdown.setMarkdown(dto.getMarkdown());
        markdown.setVersion(dto.getVersion());
        reportMarkdownService.updateById(markdown);

        baseMapper.insert(eduReport);
        return eduReport.getVersion();
    }

    @Override
    public void removeReport(Long id) {
        EduReport report = baseMapper.selectById(id);
        report.setIsRemoveAfterReview(!report.getIsRemoveAfterReview());
        baseMapper.updateById(report);
    }

    @Override
    public AdminReportPreviewDTO getReportPreviewById(Long id) {
        EduReport report = baseMapper.selectById(id);
        EduReportMarkdown markdown = reportMarkdownService.getById(id);
        EduReportPublishedMd publishedMd= reportPublishedMdService.getById(id);
        AdminReportPreviewDTO dto = new AdminReportPreviewDTO();
        BeanUtils.copyProperties(report,dto);
        dto.setHtml_br_base64(markdown.getHtmlBrBase64());
        dto.setPublished_html_br_base64(publishedMd.getHtmlBrBase64());
        return dto;
    }

    @Override
    public void switchEnableById(Long id) {
        EduReport report = baseMapper.selectById(id);
        Assert.notNull(report,"Invalid Report");
        report.setEnable(!report.getEnable());
        baseMapper.updateById(report);
    }

    @Override
    public List<ReviewBasicDTO> getInfoByReviews(List<EduReview> reviews) {

        if(reviews.isEmpty()){
            return Collections.emptyList();
        }
        List<Long> reportIds= reviews.stream().map(EduReview::getRefId).collect(Collectors.toList());
        Map<Long, EduReport> reportMap = baseMapper.selectBatchIds(reportIds).stream().collect(Collectors.toMap(EduReport::getId, Function.identity()));
        return reviews.stream().map(review->ReviewBasicDTO.builder()
                .id(review.getId())
                .review(review.getStatus())
                .title(reportMap.get(review.getRefId()).getTitle())
                .type(review.getRefType())
                .gmtCreate(review.getGmtCreate())
                .refId(review.getRefId())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public ReviewAble getReviewById(Long refId) {

        EduReport  report= baseMapper.selectById(refId);
        Assert.notNull(report,"Invalid Report");
        AvatarDTO avatar = memberService.getMemberAvatarById(refId);
        EduReportMarkdown markdown = reportMarkdownService.getById(refId);
        return ShowReportDTO.builder().title(report.getTitle())
                .html(markdown.getHtmlBrBase64())
                .videoLink(report.getVideoLink())
                .speaker(avatar)
                .build();
    }

    @Override
    public ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id) {

        EduReport report = baseMapper.selectById(id);
        Assert.notNull(report,"Report Not Found");
        EduReportMarkdown reportMd =reportMarkdownService.getById(id);
        EduReportPublished  published= publishedService.getById(id);
        EduReportPublishedMd publishedMd = reportPublishedMdService.getById(id);

        ShowReportDTO applied= ShowReportDTO.builder()
                .html(reportMd.getHtmlBrBase64())
                .videoLink(report.getVideoLink())
                .speaker(memberService.getMemberAvatarById(report.getSpeakerId()))
                .title(report.getTitle()).build();
        ShowReportDTO reviewed= ShowReportDTO.builder()
                .html(publishedMd.getHtmlBrBase64())
                .videoLink(published.getVideoLink())
                .speaker(memberService.getMemberAvatarById(published.getSpeakerId()))
                .title(published.getTitle()).build();



        return ReviewTargetDTO.<ReviewAble>builder()
                .applied(applied)
                .reviewed(reviewed)
                .status(report.getReview())
                .title(report.getTitle())
                .id(report.getId())
                .build();
    }

    @Override
    public boolean existsByIds(List<Long> ids, ReviewStatus... statuses) {
        LambdaQueryWrapper<EduReport> wrapper = Wrappers.lambdaQuery();
        wrapper.in(EduReport::getId,ids)
                .in(EduReport::getReview,List.of(statuses));
        int count = baseMapper.selectCount(wrapper).intValue();
        return count == ids.size();
    }

    @Override
    public void updateReviewedReport(List<Long> ids, ReviewStatus reviewStatus) {
        LambdaUpdateWrapper<EduReport> wrapper = Wrappers.lambdaUpdate();
        wrapper.in(EduReport::getId, ids)
                .set(EduReport::getReview, reviewStatus);
        baseMapper.update(null,wrapper);
    }

    @Override
    public void publishReviewedReport(List<Long> ids) {
        LambdaQueryWrapper<EduReport> wrapper = Wrappers.lambdaQuery();
        wrapper.in(EduReport::getId, ids)
                .eq(EduReport::getReview, ReviewStatus.APPLIED);
        List<EduReport> reports = baseMapper.selectList(wrapper);
        List<EduReportPublished> publishedList = reports.stream().map(report -> {
            EduReportPublished published = new EduReportPublished();
            BeanUtils.copyProperties(report, published);
            return published;
        }).collect(Collectors.toList());
        publishedService.saveOrUpdateBatch(publishedList);

        List<Long> reportIds = reports.stream().map(EduReport::getId).collect(Collectors.toList());
        LambdaQueryWrapper<EduReportMarkdown> mdWrapper = Wrappers.lambdaQuery();
        mdWrapper.in(EduReportMarkdown::getId, reportIds);
        List<EduReportMarkdown> markdowns = reportMarkdownService.list(mdWrapper);
        List<EduReportPublishedMd> publishedMds = markdowns.stream().map(md -> {
            EduReportPublishedMd publishedMd = new EduReportPublishedMd();
            BeanUtils.copyProperties(md, publishedMd);
            return publishedMd;
        }).collect(Collectors.toList());
        reportPublishedMdService.saveOrUpdateBatch(publishedMds);
    };



    /**
     *  check if the title is usable, in order to avoid duplicated title.
     * @param title the title to check
     * @param id the id to exclude
     * @return return true is the title have no duplicate.
     */
    private boolean checkTitleUsable(String title, Long id){

        LambdaQueryWrapper<EduReport> wrapper = Wrappers.lambdaQuery();
        if(id!=null){
            wrapper.ne(EduReport::getId, id);
        }
        wrapper.eq(EduReport::getTitle,title);
        return!baseMapper.exists(wrapper);
    }

}
