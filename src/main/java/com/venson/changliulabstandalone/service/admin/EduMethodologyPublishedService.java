package com.venson.changliulabstandalone.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.dto.MethodologyDTO;
import com.venson.changliulabstandalone.entity.dto.ReviewBasicDTO;
import com.venson.changliulabstandalone.entity.dto.ReviewDTO;
import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import com.venson.changliulabstandalone.entity.pojo.EduMethodology;
import com.venson.changliulabstandalone.entity.pojo.EduMethodologyPublished;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.vo.admin.AdminMethodologyVo;
import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.utils.PageResponse;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
public interface EduMethodologyPublishedService extends IService<EduMethodologyPublished> {

    ReviewAble getReviewById(Long refId);
}
