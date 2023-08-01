package com.venson.changliulab.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.dto.ReviewBasicDTO;
import com.venson.changliulab.entity.dto.ReviewTargetDTO;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.entity.vo.admin.AdminMethodologyVo;
import com.venson.changliulab.entity.vo.admin.CommonMetaVo;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.entity.pojo.EduMethodology;
import com.venson.changliulab.entity.dto.MethodologyPreviewDTO;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
public interface EduMethodologyService extends IService<EduMethodology> {

//    List<EduMethodology> getMethodologyReviewList();

    PageResponse<EduMethodology> getMethodologyPage(Integer page, Integer limit);
    PageResponse<EduMethodology> getMethodologyFrontPage(Integer page, Integer limit, boolean adminUser);

    MethodologyPreviewDTO getMethodologyViewById(Long id);

    Long addMethodology(AdminMethodologyVo methodology);

    void updateMethodology(Long id, AdminMethodologyVo methodology);

    PageResponse<EduMethodology> getMethodologyReviewPage(Integer current, Integer size);

    void switchEnableById(Long id);

    void switchPublicById(Long id);

    PageResponse<EduMethodology> getMethodologyPage(PageQueryVo vo);

    void removeMethodologyById(Long id);

    Collection<? extends ReviewBasicDTO> getInfoByReviews(List<EduReview> orDefault);

    ReviewAble getReviewById(Long refId);

    ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id);

    boolean existsByIds(List<Long> ids, ReviewStatus applied);

    void updateReviewStatusByIds(List<Long> ids, ReviewStatus reviewStatus);

    void publishReviewedMethodology(List<Long> ctx);

    EduMethodology getMethodologyById(Long id, CommonMetaVo vo);
}
