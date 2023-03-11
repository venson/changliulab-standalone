package com.venson.changliulabstandalone.entity.dto;

import com.venson.changliulabstandalone.entity.pojo.EduReviewMsg;
import com.venson.changliulabstandalone.entity.enums.ReviewAction;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.entity.enums.ReviewType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class ReviewApplyVo {
    @NotNull
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
