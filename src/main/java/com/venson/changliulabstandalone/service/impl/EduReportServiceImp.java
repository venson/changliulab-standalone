package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulabstandalone.entity.EduReport;
import com.venson.changliulabstandalone.entity.EduReportMarkdown;
import com.venson.changliulabstandalone.entity.EduReportPublishedMd;
import com.venson.changliulabstandalone.entity.dto.AdminReportDTO;
import com.venson.changliulabstandalone.entity.dto.AdminReportPreviewDTO;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.enums.PageType;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.pojo.EduResearch;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.mapper.EduReportMapper;
import com.venson.changliulabstandalone.service.EduReportMarkdownService;
import com.venson.changliulabstandalone.service.EduReportPublishedMdService;
import com.venson.changliulabstandalone.service.EduReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author venson
 * @since 2023-03-23
 */
@Service
public class EduReportServiceImp extends ServiceImpl<EduReportMapper, EduReport> implements EduReportService {
    @Autowired
    private EduReportMarkdownService reportMarkdownService;
    @Autowired
    private EduReportPublishedMdService reportPublishedMdService;

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
    public Collection<? extends ReviewBasicDTO> getInfoByReviews(List<EduReview> reviews) {

        if(reviews.isEmpty()){
            return Collections.emptyList();
        }
        List<Long> reportIds= reviews.stream().map(EduReview::getRefId).collect(Collectors.toList());
        Map<Long, EduReport> reportMap = baseMapper.selectBatchIds(reportIds).stream().collect(Collectors.toMap(EduReport::getId, Function.identity()));
        return reviews.stream().map(review->ReviewBasicDTO.builder()
                .id(review.getId())
                .review(review.getStatus())
                .title(reportMap.get(review.getRefId()).getTitle())
                .gmtCreate(review.getGmtCreate())
                .refId(review.getRefId())
                .build()
        ).collect(Collectors.toList());
    }

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
