package com.venson.changliulab.utils;

import com.venson.changliulab.entity.enums.ReviewStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StateMachineConst {
    public static final Set<ReviewStatus> NONE= new HashSet<>(List.of(ReviewStatus.NONE, ReviewStatus.FINISHED));
}
