package com.venson.changliulab.service.front;

import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.entity.pojo.EduScholar;
import com.venson.changliulab.entity.front.dto.ScholarFrontDTO;
import com.venson.changliulab.entity.front.vo.CitationFrontVo;
import com.venson.changliulab.entity.front.vo.ScholarFrontFilterVo;

public interface ScholarFrontService {
    PageResponse<EduScholar> getPageScholarByMemberId(Long id, Integer page, Integer limit);

    ScholarFrontDTO getScholarDTOById(String scholarId);

    PageResponse<EduScholar> getPageScholarWithFilter(Integer pageNum, Integer limit, ScholarFrontFilterVo filterVo);
    PageResponse<EduScholar> doGetPageScholar(Integer pageNum, Integer limit);

    CitationFrontVo getCitationByMemberId(Long memberId);
}
