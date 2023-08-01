package com.venson.changliulab.service.admin;

import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduActivityPublished;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.front.dto.ActivityFrontBriefDTO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2022-08-09
 */
public interface EduActivityPublishedService extends IService<EduActivityPublished> {

//    PageResponse<EduActivityPublished> getPageActivityList(Integer page, Integer limit);

    List<ActivityFrontBriefDTO> getFrontIndexActivity();

    ReviewAble getReviewById(Long refId);
}
