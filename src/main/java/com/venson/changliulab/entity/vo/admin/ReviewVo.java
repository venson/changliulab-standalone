package com.venson.changliulab.entity.vo.admin;

import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;

import java.util.List;

public record ReviewVo(String message, List<ReviewItemVo> reviews, ReviewStatus from, ReviewAction action) {
}
