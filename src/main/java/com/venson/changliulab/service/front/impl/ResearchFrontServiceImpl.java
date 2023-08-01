package com.venson.changliulab.service.front.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.front.dto.ResearchFrontDTO;
import com.venson.changliulab.entity.pojo.EduMember;
import com.venson.changliulab.entity.pojo.EduResearchMember;
import com.venson.changliulab.entity.pojo.EduResearchPublished;
import com.venson.changliulab.service.admin.EduMemberService;
import com.venson.changliulab.service.admin.EduResearchMemberService;
import com.venson.changliulab.service.admin.EduResearchPublishedService;
import com.venson.changliulab.service.admin.EduResearchService;
import com.venson.changliulab.service.front.ResearchFrontService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Service
@RequiredArgsConstructor
public class ResearchFrontServiceImpl implements ResearchFrontService {
    private final  EduResearchService researchService;

    private final EduResearchMemberService researchMemberService;
    private final EduMemberService memberService;
    private final EduResearchPublishedService researchPublishedService;
    @Override
    @Cacheable(value = FrontCacheConst.RESEARCH_NAME, key= FrontCacheConst.RESEARCH_HOME_KEY)
    public List<ResearchFrontDTO> getResearchList() {
        LambdaQueryWrapper<EduResearchPublished> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(EduResearchPublished::getEnable, true)
                .select(EduResearchPublished::getHtmlBrBase64, EduResearchPublished::getId, EduResearchPublished::getTitle);
        List<EduResearchPublished> researches = researchPublishedService.list(wrapper);
        return buildResearchFrontDTOS(researches);
    }

    private List<ResearchFrontDTO> buildResearchFrontDTOS(List<EduResearchPublished> researches) {
        if(researches ==null || researches.size()==0){
            return null;
        }
        Set<Long> researchIds = researches.stream().map(EduResearchPublished::getId).collect(Collectors.toSet());
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
        LambdaQueryWrapper<EduResearchPublished> wrapper = Wrappers.lambdaQuery();
        if(researchIds.size()>0){
            wrapper.in( EduResearchPublished::getId,researchIds).eq(EduResearchPublished::getEnable, true)
                    .select(EduResearchPublished::getHtmlBrBase64, EduResearchPublished::getId, EduResearchPublished::getTitle);

            List<EduResearchPublished> researches = researchPublishedService.list(wrapper);
            return buildResearchFrontDTOS(researches);
        }
        return null;


    }
}
