package com.venson.changliulab.service.front;

import com.venson.changliulab.entity.front.dto.ResearchFrontDTO;

import java.util.List;

public interface ResearchFrontService {
    List<ResearchFrontDTO> getResearchList();

    List<ResearchFrontDTO> getResearchListByMemberId(Long id);
}
