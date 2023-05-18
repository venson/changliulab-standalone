package com.venson.changliulabstandalone.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.dto.ReviewDTO;
import com.venson.changliulabstandalone.entity.enums.ReviewType;
import com.venson.changliulabstandalone.entity.vo.admin.ListQueryParams;
import com.venson.changliulabstandalone.utils.PageResponse;

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


    List<ReviewDTO> getReviewListByRefId(Long id, ReviewType type);

    PageResponse<ReviewBasicDTO> getPageReview(ListQueryParams params);

    ReviewDTO<ReviewAble> getReviewById(Long id);
}
