package com.venson.changliulab.service.front;

import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.entity.pojo.EduActivityPublished;
import com.venson.changliulab.entity.front.dto.ActivityFrontBriefDTO;
import com.venson.changliulab.entity.front.dto.ActivityFrontDTO;

import java.util.List;

public interface ActivityFrontService {
    PageResponse<EduActivityPublished> getPageActivity(Integer page, Integer limit);

    ActivityFrontDTO getActivityById(Long id);

    List<ActivityFrontBriefDTO> getFrontIndexActivity();
}
