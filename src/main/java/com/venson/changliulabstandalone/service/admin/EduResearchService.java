package com.venson.changliulabstandalone.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.dto.AdminResearchDTO;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.entity.pojo.EduResearch;
import com.venson.changliulabstandalone.entity.dto.ResearchDTO;

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
public interface EduResearchService extends IService<EduResearch> {


//    List<EduResearch> getResearchReviewList();

    PageResponse<EduResearch> getResearchPage(Integer page, Integer limit);

    Long addResearch(AdminResearchDTO research);

    void updateResearch(Long id, AdminResearchDTO research);

    ResearchDTO getResearchPreviewById(Long id);

    PageResponse<EduResearch> getResearchReviewPage(PageQueryVo vo);

    void switchEnableById(Long id);

    void removeResearchById(Long id);

    AdminResearchDTO getResearchById(Long id);
    PageResponse<EduResearch> getResearchPage(PageQueryVo pageQueryVo);

    List<ReviewBasicDTO> getInfoByReviews(List<EduReview> orDefault);
}
