package com.venson.changliulabstandalone.service.front;

import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.entity.pojo.EduActivityPublished;
import com.venson.changliulabstandalone.entity.front.dto.ActivityFrontBriefDTO;
import com.venson.changliulabstandalone.entity.front.dto.ActivityFrontDTO;

import java.util.List;

public interface ActivityFrontService {
    PageResponse<EduActivityPublished> getPageActivity(Integer page, Integer limit);

    ActivityFrontDTO getActivityById(Long id);

    List<ActivityFrontBriefDTO> getFrontIndexActivity();
}
