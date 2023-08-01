package com.venson.changliulab.service.admin;

import com.venson.changliulab.entity.dto.ReviewTargetDTO;
import com.venson.changliulab.entity.inter.ReviewAble;

public interface ReviewableService {
    ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id);
}
