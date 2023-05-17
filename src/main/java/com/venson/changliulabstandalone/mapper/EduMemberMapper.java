package com.venson.changliulabstandalone.mapper;

import com.venson.changliulabstandalone.config.UserBaseMapper;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.venson.changliulabstandalone.entity.front.dto.MemberFrontBriefDTO;

import java.util.List;

/**
 * <p>
 * 讲师 Mapper 接口
 * </p>
 *
 * @author baomidou
 * @since 2022-05-02
 */
public interface EduMemberMapper extends UserBaseMapper<EduMember> {

    List<MemberFrontBriefDTO> getFrontIndexMember();
}
