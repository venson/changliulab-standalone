package com.venson.changliulab.service.admin;

import com.venson.changliulab.entity.dto.AvatarDTO;
import com.venson.changliulab.entity.pojo.EduResearchMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.vo.BasicMemberVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author venson
 * @since 2023-02-27
 */
public interface EduResearchMemberService extends IService<EduResearchMember> {

//    List<BasicMemberVo> getMembersByResearchId(Long id);

    void addResearchMembers(Long researchId, List<Long> members);

    void updateResearchMembers(Long id, List<Long> members);

    List<AvatarDTO> getFullMembersByResearchId(Long id, boolean latest);
}
