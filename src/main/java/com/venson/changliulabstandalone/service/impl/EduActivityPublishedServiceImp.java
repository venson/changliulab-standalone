package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.entity.pojo.EduActivityPublished;
import com.venson.changliulabstandalone.entity.front.dto.ActivityFrontBriefDTO;
import com.venson.changliulabstandalone.mapper.EduActivityPublishedMapper;
import com.venson.changliulabstandalone.service.admin.EduActivityPublishedService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-08-09
 */
@Service
public class EduActivityPublishedServiceImp extends ServiceImpl<EduActivityPublishedMapper, EduActivityPublished> implements EduActivityPublishedService {

//    @Override
//    public PageResponse<EduActivityPublished> getPageActivityList(Integer page, Integer limit) {
//        Page<EduActivityPublished> pageActivity = new Page<>(page, limit);
//        LambdaQueryWrapper<EduActivityPublished> wrapper = new LambdaQueryWrapper<>();
//        wrapper.orderByDesc(EduActivityPublished::getId);
//        wrapper.eq(EduActivityPublished::getIsPublished,true);
//        baseMapper.selectPage(pageActivity, wrapper);
//        return PageUtil.toBean(pageActivity);
//    }

    @Override
    public List<ActivityFrontBriefDTO> getFrontIndexActivity() {
        return baseMapper.getFrontIndexActivity();
    }
}
