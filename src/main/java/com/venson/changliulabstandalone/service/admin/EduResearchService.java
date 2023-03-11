package com.venson.changliulabstandalone.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.dto.AdminResearchDTO;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.entity.pojo.EduResearch;
import com.venson.changliulabstandalone.entity.dto.ResearchDTO;
import com.venson.changliulabstandalone.entity.enums.LanguageEnum;

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

    PageResponse<EduResearch> getResearchReviewPage(Integer current, Integer size);

    void switchEnableById(Long id, LanguageEnum lang);

    void removeResearchById(Long id);

    AdminResearchDTO getResearchById(Long id);
}
