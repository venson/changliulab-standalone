package com.venson.changliulabstandalone.service;

import com.venson.changliulabstandalone.entity.EduReport;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.dto.AdminReportDTO;
import com.venson.changliulabstandalone.entity.dto.AdminReportPreviewDTO;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.enums.PageType;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.utils.PageResponse;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2023-03-23
 */
public interface EduReportService extends IService<EduReport> {

    PageResponse<EduReport> getPageReport(int current, int size, PageType type);


    AdminReportDTO getReportById(long id);

    Long addReport(AdminReportDTO dto);

    long updateReport(Long id, AdminReportDTO dto);

    void removeReport(Long id);

    AdminReportPreviewDTO getReportPreviewById(Long id);

    void switchEnableById(Long id);

    Collection<? extends ReviewBasicDTO> getInfoByReviews(List<EduReview> orDefault);
}
