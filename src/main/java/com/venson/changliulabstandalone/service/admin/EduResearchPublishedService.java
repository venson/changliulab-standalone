package com.venson.changliulabstandalone.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.dto.AdminResearchDTO;
import com.venson.changliulabstandalone.entity.dto.ResearchDTO;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.dto.ReviewDTO;
import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import com.venson.changliulabstandalone.entity.pojo.EduResearch;
import com.venson.changliulabstandalone.entity.pojo.EduResearchPublished;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.utils.PageResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
public interface EduResearchPublishedService extends IService<EduResearchPublished> {


    ReviewAble getReviewById(Long refId);
}
