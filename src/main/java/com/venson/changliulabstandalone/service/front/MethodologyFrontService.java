package com.venson.changliulabstandalone.service.front;

import com.venson.changliulabstandalone.entity.pojo.EduMethodology;
import com.venson.changliulabstandalone.entity.front.dto.MethodologyFrontDTO;
import com.venson.changliulabstandalone.utils.PageResponse;

public interface MethodologyFrontService {

    PageResponse<EduMethodology> getPageMethodology(Integer current, Integer size);

    MethodologyFrontDTO getMethodologyById(Long id);

}
