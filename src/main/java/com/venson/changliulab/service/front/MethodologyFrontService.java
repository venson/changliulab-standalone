package com.venson.changliulab.service.front;

import com.venson.changliulab.entity.pojo.EduMethodology;
import com.venson.changliulab.entity.front.dto.MethodologyFrontDTO;
import com.venson.changliulab.utils.PageResponse;

public interface MethodologyFrontService {

    PageResponse<EduMethodology> getPageMethodology(Integer current, Integer size);

    MethodologyFrontDTO getMethodologyById(Long id);

}
