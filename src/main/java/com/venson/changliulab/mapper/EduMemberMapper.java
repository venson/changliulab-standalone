package com.venson.changliulab.mapper;

import com.venson.changliulab.config.UserBaseMapper;
import com.venson.changliulab.entity.pojo.EduMember;
import com.venson.changliulab.entity.front.dto.MemberFrontBriefDTO;

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
