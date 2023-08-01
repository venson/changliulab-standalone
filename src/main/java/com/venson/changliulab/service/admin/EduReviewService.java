package com.venson.changliulab.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.dto.ReviewBasicDTO;
import com.venson.changliulab.entity.dto.ReviewTargetDTO;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.entity.dto.ReviewDTO;
import com.venson.changliulab.entity.vo.admin.ListQueryParams;
import com.venson.changliulab.entity.vo.admin.ReviewMetaVo;
import com.venson.changliulab.utils.PageResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2022-07-16
 */
public interface EduReviewService extends IService<EduReview> {




    List<EduReview> getReviewByResearchId(Long id);




    List<EduReview> getReviewByMethodologyId(Long id);


//    List<ReviewDTO> getReviewListByRefId(Long id, ReviewType type);

    PageResponse<ReviewBasicDTO> getPageReview(ListQueryParams params);

    ReviewDTO<ReviewAble> getReviewById(Long id, ReviewMetaVo metaVo);

    ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id, ReviewMetaVo metaVo);
}
