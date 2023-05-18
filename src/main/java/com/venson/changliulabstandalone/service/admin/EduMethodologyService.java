package com.venson.changliulabstandalone.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.dto.ReviewDTO;
import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.vo.admin.AdminMethodologyVo;
import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.entity.pojo.EduMethodology;
import com.venson.changliulabstandalone.entity.dto.MethodologyDTO;

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

    MethodologyDTO getMethodologyViewById(Long id);

    Long addMethodology(AdminMethodologyVo methodology);

    void updateMethodology(Long id, AdminMethodologyVo methodology);

    PageResponse<EduMethodology> getMethodologyReviewPage(Integer current, Integer size);

    void switchEnableById(Long id);

    void switchPublicById(Long id);

    PageResponse<EduMethodology> getMethodologyPage(PageQueryVo vo);

    void removeMethodologyById(Long id);

    Collection<? extends ReviewBasicDTO> getInfoByReviews(List<EduReview> orDefault);

    ReviewAble getReviewById(Long refId);
}
