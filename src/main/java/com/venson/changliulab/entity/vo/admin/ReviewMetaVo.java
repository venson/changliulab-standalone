package com.venson.changliulab.entity.vo.admin;

import com.venson.changliulab.entity.enums.ReviewType;

public record ReviewMetaVo (ReviewType type, Long metaId, ReviewType metaType){}
