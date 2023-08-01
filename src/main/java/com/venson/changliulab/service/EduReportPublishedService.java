package com.venson.changliulab.service;

import com.venson.changliulab.entity.pojo.EduReportPublished;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.front.dto.ReportFrontDTO;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.utils.PageResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2023-03-23
 */
public interface EduReportPublishedService extends IService<EduReportPublished> {

    PageResponse<EduReportPublished> getPageFrontReport(int current, int size);

    ReportFrontDTO getFrontReportById(int id);

    ReviewAble getReviewById(Long refId);
}
