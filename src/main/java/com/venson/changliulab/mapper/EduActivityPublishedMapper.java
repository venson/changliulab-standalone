package com.venson.changliulab.mapper;

import com.venson.changliulab.entity.pojo.EduActivityPublished;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.venson.changliulab.entity.front.dto.ActivityFrontBriefDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author venson
 * @since 2022-08-09
 */
@Mapper
public interface EduActivityPublishedMapper extends BaseMapper<EduActivityPublished> {

    List<ActivityFrontBriefDTO> getFrontIndexActivity();
}
