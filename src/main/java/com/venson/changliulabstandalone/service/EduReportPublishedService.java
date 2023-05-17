package com.venson.changliulabstandalone.service;

import com.venson.changliulabstandalone.entity.EduReportPublished;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.front.dto.ReportFrontDTO;
import com.venson.changliulabstandalone.utils.PageResponse;

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
}
