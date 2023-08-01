package com.venson.changliulab.service.front;

import com.venson.changliulab.entity.front.dto.MemberFrontDTO;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.entity.pojo.EduMember;
import com.venson.changliulab.entity.enums.MemberLevel;
import com.venson.changliulab.entity.front.dto.MemberFrontBriefDTO;

import java.util.List;

public interface MemberFrontService {
//    List<EduMember> getPIMemberFront();

    PageResponse<EduMember> getPageFrontMemberListByLevel(Integer page, Integer limit, MemberLevel level);


    List<MemberFrontBriefDTO> getFrontIndexMember();


    MemberFrontDTO getMemberFrontDTOByID(Long id, Integer sPage, Integer cPage, Integer sSize, Integer cSize);
}
