package com.venson.changliulabstandalone.service.admin;

import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.pojo.EduResearchMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.vo.BasicMemberVo;

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

    List<BasicMemberVo> getMembersByResearchId(Long id);

    void addResearchMembers(Long researchId, List<BasicMemberVo> members);

    void updateResearchMembers(Long id, List<BasicMemberVo> members);

    List<EduMember> getFullMembersByResearchId(Long id);
}
