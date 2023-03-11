package com.venson.changliulabstandalone.service.admin;

import com.venson.changliulabstandalone.entity.vo.admin.AdminMemberVo;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.dto.MemberQuery;
import com.venson.changliulabstandalone.entity.front.dto.MemberFrontBriefDTO;
import com.venson.changliulabstandalone.entity.vo.MemberVo;

import java.util.List;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author baomidou
 * @since 2022-05-02
 */
public interface EduMemberService extends IService<EduMember> {

    PageResponse<EduMember> getMemberPage(Integer pageNum, Integer limit, MemberQuery memberQuery);

    void updateMember(Long id, MemberVo member);

    List<MemberFrontBriefDTO> getFrontIndexMember();

    List<EduMember> getCurrentMember();

    List<EduMember> getAllMember();

    MemberFrontBriefDTO getMemberFrontById(Long id);

    void addMember(AdminMemberVo eduMember);
}
