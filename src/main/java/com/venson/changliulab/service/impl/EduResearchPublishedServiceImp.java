package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.entity.dto.*;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduMember;
import com.venson.changliulab.entity.pojo.EduResearchPublished;
import com.venson.changliulab.mapper.EduResearchPublishedMapper;
import com.venson.changliulab.service.admin.EduResearchMemberService;
import com.venson.changliulab.service.admin.EduResearchPublishedService;
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
        if(eduResearch == null) return null;
        ShowResearchDTO reviewed = new ShowResearchDTO();
        reviewed.setHtml(eduResearch.getHtmlBrBase64());
        reviewed.setTitle(eduResearch.getTitle());
        List<AvatarDTO> members = researchMemberService.getFullMembersByResearchId(id, false);
        reviewed.setMembers(members);
        return reviewed;
    }
}
