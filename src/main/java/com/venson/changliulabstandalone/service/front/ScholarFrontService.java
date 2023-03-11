package com.venson.changliulabstandalone.service.front;

import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.entity.pojo.EduScholar;
import com.venson.changliulabstandalone.entity.front.dto.ScholarFrontDTO;
import com.venson.changliulabstandalone.entity.front.vo.CitationFrontVo;
import com.venson.changliulabstandalone.entity.front.vo.ScholarFrontFilterVo;

public interface ScholarFrontService {
    PageResponse<EduScholar> getPageScholarByMemberId(Long id, Integer page, Integer limit);

    ScholarFrontDTO getScholarDTOById(String scholarId);

    PageResponse<EduScholar> getPageScholarWithFilter(Integer pageNum, Integer limit, ScholarFrontFilterVo filterVo);
    PageResponse<EduScholar> doGetPageScholar(Integer pageNum, Integer limit);

    CitationFrontVo getCitationByMemberId(Long memberId);
}
