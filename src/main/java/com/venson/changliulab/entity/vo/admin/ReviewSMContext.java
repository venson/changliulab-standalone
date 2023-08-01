package com.venson.changliulab.entity.vo.admin;

import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.enums.ReviewType;

import java.util.List;

public record ReviewSMContext(List<Long> ids, ReviewType type, String message, ReviewStatus from) {
}
