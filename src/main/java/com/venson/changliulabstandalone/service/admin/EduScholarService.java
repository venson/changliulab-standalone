package com.venson.changliulabstandalone.service.admin;

import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.entity.pojo.EduScholar;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.dto.ScholarAdminDTO;
import com.venson.changliulabstandalone.entity.vo.ScholarFilterVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2022-06-18
 */
public interface EduScholarService extends IService<EduScholar> {
    PageResponse<EduScholar> getPageScholar(Integer page, Integer limit, ScholarFilterVo filterVo);

    ScholarAdminDTO getScholarById(Long id);

    void updateScholar(ScholarAdminDTO scholar);

    Long addScholar(ScholarAdminDTO scholar);
}
