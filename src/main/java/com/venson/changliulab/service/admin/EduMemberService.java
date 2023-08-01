package com.venson.changliulab.service.admin;

import com.venson.changliulab.entity.dto.AvatarDTO;
import com.venson.changliulab.entity.vo.admin.AdminMemberVo;
import com.venson.changliulab.entity.vo.admin.ListQueryParams;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.entity.pojo.EduMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.dto.MemberQuery;
import com.venson.changliulab.entity.front.dto.MemberFrontBriefDTO;
import com.venson.changliulab.entity.vo.MemberVo;

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

    Long addMember(AdminMemberVo eduMember);

    PageResponse<EduMember> getMemberPage(ListQueryParams pageVo);

    EduMember getMemberById(Long id);

    AvatarDTO getMemberAvatarById(long id);
}
