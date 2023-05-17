package com.venson.changliulabstandalone.utils;

import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StateMachineConst {
    public static final Set<ReviewStatus> NONE= new HashSet<>(List.of(ReviewStatus.NONE, ReviewStatus.FINISHED));
}
