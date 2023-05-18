package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.entity.dto.*;
import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.pojo.EduResearchPublished;
import com.venson.changliulabstandalone.mapper.EduResearchPublishedMapper;
import com.venson.changliulabstandalone.service.admin.EduResearchMemberService;
import com.venson.changliulabstandalone.service.admin.EduResearchPublishedService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-07-04
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EduResearchPublishedServiceImp extends ServiceImpl<EduResearchPublishedMapper, EduResearchPublished> implements EduResearchPublishedService {

    private final EduResearchMemberService researchMemberService;


    @Override
    public ReviewAble getReviewById(Long id) {
        EduResearchPublished eduResearch = baseMapper.selectById(id);
        ShowResearchDTO reviewed = new ShowResearchDTO();
        reviewed.setHtml(eduResearch.getHtmlBrBase64());
        reviewed.setTitle(eduResearch.getTitle());
        List<EduMember> members = researchMemberService.getFullMembersByResearchId(id);
        reviewed.setMembers(members);
        return reviewed;
    }
}
