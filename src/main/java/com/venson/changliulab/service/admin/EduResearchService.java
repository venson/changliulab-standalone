package com.venson.changliulab.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.dto.AdminResearchDTO;
import com.venson.changliulab.entity.dto.ResearchPreviewDTO;
import com.venson.changliulab.entity.dto.ReviewBasicDTO;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduResearch;
import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.entity.vo.admin.CommonMetaVo;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.utils.PageResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
public interface EduResearchService extends IService<EduResearch> {


//    List<EduResearch> getResearchReviewList();

    PageResponse<EduResearch> getResearchPage(Integer page, Integer limit);

    Long addResearch(AdminResearchDTO research);

    void updateResearch(Long id, AdminResearchDTO research);

    ResearchPreviewDTO getResearchPreviewById(Long id);

    PageResponse<EduResearch> getResearchReviewPage(PageQueryVo vo);

    void switchEnableById(Long id);

    void removeResearchById(Long id);

    AdminResearchDTO getResearchById(Long id, CommonMetaVo vo);
    PageResponse<EduResearch> getResearchPage(PageQueryVo pageQueryVo);

    List<ReviewBasicDTO> getInfoByReviews(List<EduReview> orDefault);

    ReviewAble getReviewById(Long refId);

    void updateReviewedResearch(List<Long> ids, ReviewStatus reviewStatus);

    boolean existsByIds(List<Long> ids, ReviewStatus... reviewStatus);

    void publishReviewedResearch(List<Long> ids);

//    ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id);
}
