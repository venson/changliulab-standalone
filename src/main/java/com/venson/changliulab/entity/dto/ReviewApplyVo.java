package com.venson.changliulab.entity.dto;

import com.venson.changliulab.entity.pojo.EduReviewMsg;
import com.venson.changliulab.entity.enums.ReviewAction;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.enums.ReviewType;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ReviewApplyVo {
    @NotNull
    @Parameter(description = "target content id")
    private Long id;
    @NotNull
    private ReviewType type;
    @NotNull
    private Long userId;
    @NotBlank
    private String userName;
    private String msg;
    private Long parentId;
    private ReviewType parentType;

    private ReviewStatus from;
    private ReviewStatus to;


    private ReviewAction action;

    public void copyRequestMsgToEduReviewMsg(EduReviewMsg msg){
        msg.setRequestUserId(this.userId);
        msg.setRequestUsername(this.userName);
        msg.setRequestMsg(this.msg);
        msg.setRefId(this.id);
        msg.setRefType(this.type);
    }
    public void copyReviewMsgToEduReviewMsg(EduReviewMsg msg){
        msg.setReviewUserId(this.userId);
        msg.setReviewUsername(this.userName);
        msg.setReviewMsg(this.msg);
        msg.setReviewAction(this.action);
    }
}
