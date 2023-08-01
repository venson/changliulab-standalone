package com.venson.changliulab.service.admin;

import com.venson.changliulab.entity.vo.admin.ListQueryParams;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.entity.pojo.EduScholar;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.dto.ScholarAdminDTO;
import com.venson.changliulab.entity.vo.ScholarFilterVo;

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

    PageResponse<EduScholar> getPageScholar(ListQueryParams queryParams);
}
