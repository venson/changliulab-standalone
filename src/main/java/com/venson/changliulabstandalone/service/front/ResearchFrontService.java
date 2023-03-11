package com.venson.changliulabstandalone.service.front;

import com.venson.changliulabstandalone.entity.front.dto.ResearchFrontDTO;

import java.util.List;

public interface ResearchFrontService {
    List<ResearchFrontDTO> getResearchList();

    List<ResearchFrontDTO> getResearchListByMemberId(Long id);
}
