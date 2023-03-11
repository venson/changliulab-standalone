package com.venson.changliulabstandalone.service.front.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.pojo.EduResearch;
import com.venson.changliulabstandalone.entity.pojo.EduResearchMember;
import com.venson.changliulabstandalone.entity.front.dto.ResearchFrontDTO;
import com.venson.changliulabstandalone.service.admin.EduMemberService;
import com.venson.changliulabstandalone.service.admin.EduResearchMemberService;
import com.venson.changliulabstandalone.service.admin.EduResearchService;
import com.venson.changliulabstandalone.service.front.ResearchFrontService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class ResearchFrontServiceImpl implements ResearchFrontService {
    @Autowired
    private EduResearchService researchService;

    @Autowired
    private EduResearchMemberService researchMemberService;
    @Autowired
    private EduMemberService memberService;
    @Override
    @Cacheable(value = FrontCacheConst.RESEARCH_NAME, key= FrontCacheConst.RESEARCH_HOME_KEY)
    public List<ResearchFrontDTO> getResearchList() {
        LambdaQueryWrapper<EduResearch> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(EduResearch::getEnable, true).eq(EduResearch::getIsPublished, true)
                .select(EduResearch::getPublishedHtmlBrBase64, EduResearch::getId, EduResearch::getTitle);
        List<EduResearch> researches = researchService.list(wrapper);
        return buildResearchFrontDTOS(researches);
    }

    private List<ResearchFrontDTO> buildResearchFrontDTOS(List<EduResearch> researches) {
        if(researches ==null || researches.size()==0){
            return null;
        }
        Set<Long> researchIds = researches.stream().map(EduResearch::getId).collect(Collectors.toSet());
        LambdaQueryWrapper<EduResearchMember> researchMemberWrapper = Wrappers.lambdaQuery();
        researchMemberWrapper.in(EduResearchMember::getResearchId, researchIds).select(EduResearchMember::getMemberId,EduResearchMember::getResearchId);
        List<EduResearchMember> researchMembers = researchMemberService.list(researchMemberWrapper);
        Map<Long, List<Long>> researchIdMemberIdsMap;
        List<Long> memberIds;
        List<EduMember> members;
        Map<Long, EduMember> memberMap;
        if(researchMembers != null){
            memberIds = researchMembers.stream().map(EduResearchMember::getMemberId).collect(toList());
            researchIdMemberIdsMap = researchMembers.stream()
                    .collect(toMap(EduResearchMember::getResearchId, o -> Collections.singletonList(o.getMemberId()),
                            (a, b) -> {
                                List<Long> result = new LinkedList<>(a);
                                result.addAll(b);
                                return result;
                            }));
             members = memberService.listByIds(memberIds);
            memberMap = members.stream().collect(Collectors.toMap(EduMember::getId, Function.identity()));
        } else {
            researchIdMemberIdsMap = null;
            memberMap = null;
        }
        return researches.stream().map(
                research -> {
                    ResearchFrontDTO dto = new ResearchFrontDTO();
                    BeanUtils.copyProperties(research, dto);
                    if(researchIdMemberIdsMap !=null){
                        List<EduMember> membersTemp = researchIdMemberIdsMap.getOrDefault(research.getId(), Collections.emptyList()).stream().map(memberMap::get).collect(Collectors.toList());
                        dto.setMembers(membersTemp);
                    }
                    return dto;
                }
        ).collect(toList());
    }

    @Override
    @Cacheable(value = FrontCacheConst.RESEARCH_NAME,key = FrontCacheConst.RESEARCH_MEMBER_KEY + "+#id")
    public List<ResearchFrontDTO> getResearchListByMemberId(Long id) {
        LambdaQueryWrapper<EduResearchMember> researchMemberWrapper = Wrappers.lambdaQuery();
        researchMemberWrapper.eq(EduResearchMember::getMemberId,id).select(EduResearchMember::getResearchId);
//        List<EduResearchMember> researchMembers = researchMemberService.list(researchMemberWrapper);
        List<Long> researchIds = researchMemberService.listObjs(researchMemberWrapper, o -> Long.valueOf(o.toString()));
        LambdaQueryWrapper<EduResearch> wrapper = Wrappers.lambdaQuery();
        if(researchIds.size()>0){
            wrapper.in( EduResearch::getId,researchIds).eq(EduResearch::getEnable, true).eq(EduResearch::getIsPublished, true)
                    .select(EduResearch::getPublishedHtmlBrBase64, EduResearch::getId, EduResearch::getTitle);

            List<EduResearch> researches = researchService.list(wrapper);
            return buildResearchFrontDTOS(researches);
        }
        return null;


    }
}
