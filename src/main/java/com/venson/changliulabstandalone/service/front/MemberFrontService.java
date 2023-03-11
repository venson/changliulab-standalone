package com.venson.changliulabstandalone.service.front;

import com.venson.changliulabstandalone.entity.front.dto.MemberFrontDTO;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.enums.MemberLevel;
import com.venson.changliulabstandalone.entity.front.dto.MemberFrontBriefDTO;

import java.util.List;

public interface MemberFrontService {
//    List<EduMember> getPIMemberFront();

    PageResponse<EduMember> getPageFrontMemberListByLevel(Integer page, Integer limit, MemberLevel level);


    List<MemberFrontBriefDTO> getFrontIndexMember();


    MemberFrontDTO getMemberFrontDTOByID(Long id, Integer sPage, Integer cPage, Integer sSize, Integer cSize);
}
