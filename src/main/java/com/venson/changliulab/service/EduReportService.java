package com.venson.changliulab.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.dto.ReviewTargetDTO;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.pojo.EduReport;
import com.venson.changliulab.entity.dto.AdminReportDTO;
import com.venson.changliulab.entity.dto.AdminReportPreviewDTO;
import com.venson.changliulab.entity.dto.ReviewBasicDTO;
import com.venson.changliulab.entity.enums.PageType;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.utils.PageResponse;

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

    List<ReviewBasicDTO> getInfoByReviews(List<EduReview> orDefault);

    ReviewAble getReviewById(Long refId);

    ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id);

    boolean existsByIds(List<Long> ids, ReviewStatus... statuses);


    void updateReviewedReport(List<Long> ids, ReviewStatus reviewStatus);

    void publishReviewedReport(List<Long> ids);
}
