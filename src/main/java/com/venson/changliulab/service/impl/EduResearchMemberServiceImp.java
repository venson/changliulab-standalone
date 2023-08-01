package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulab.entity.dto.AvatarDTO;
import com.venson.changliulab.entity.enums.ResearchMemberStatus;
import com.venson.changliulab.entity.pojo.EduMember;
import com.venson.changliulab.entity.pojo.EduResearchMember;
import com.venson.changliulab.entity.vo.BasicMemberVo;
import com.venson.changliulab.mapper.EduResearchMemberMapper;
import com.venson.changliulab.service.admin.EduMemberService;
import com.venson.changliulab.service.admin.EduResearchMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author venson
 * @since 2023-02-27
 */
@Service
@RequiredArgsConstructor
public class EduResearchMemberServiceImp extends ServiceImpl<EduResearchMemberMapper, EduResearchMember> implements EduResearchMemberService {
    private final EduMemberService memberService;

//    @Override
//    public List<BasicMemberVo> getMembersByResearchId(Long id) {
//
//        LambdaQueryWrapper<EduResearchMember> wrapper = Wrappers.lambdaQuery();
//        wrapper.eq(EduResearchMember::getResearchId, id).select(EduResearchMember::getMemberId, EduResearchMember::get);
//
//        List<EduResearchMember> researchMembers = baseMapper.selectList(wrapper);
//        LinkedList<BasicMemberVo> members = new LinkedList<>();
//        if(researchMembers.size()>0){
//            researchMembers.forEach(member -> {
//                BasicMemberVo temp = new BasicMemberVo(member.getMemberId(), member.getMemberName());
//                members.add(temp);
//            });
//        }
//        return members;
//    }

    @Transactional
    @Override
    public void addResearchMembers(Long researchId, List<Long> memberIds) {
        if( memberIds.size() > 0){
//            LinkedList<EduResearchMember> researchMembers = new LinkedList<>();
//            memberIds.forEach(member ->{
//                EduResearchMember temp = new EduResearchMember();
//                temp.setResearchId(researchId);
//                temp.setMemberId(temp.getMemberId());
////                temp.setMemberName(temp.getMemberName());
//                researchMembers.add(temp);
//            });
            List<EduResearchMember> researchMemberAddList= memberIds.stream().map(memberId -> {
                EduResearchMember temp = new EduResearchMember();
                temp.setStatus(ResearchMemberStatus.PENDING);
                temp.setMemberId(memberId);
                temp.setResearchId(researchId);
                return temp;
            }).collect(Collectors.toList());
            saveOrUpdateBatch(researchMemberAddList);
        }

    }

    @Override
    @Transactional
    public void updateResearchMembers(Long id, List<Long> members) {
        LambdaQueryWrapper<EduResearchMember> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(EduResearchMember::getResearchId, id);

        // remove all members for the research
        if(members.size()==0) {
            baseMapper.delete(wrapper);
            return;
        }
        List<EduResearchMember> old = baseMapper.selectList(wrapper);
        Map<Long,Long> oldMemberIdResearchIdMap;
        //
        if(old != null) {
            oldMemberIdResearchIdMap= old.stream().collect(Collectors.toMap(EduResearchMember::getMemberId, EduResearchMember::getId));
            LinkedList<EduResearchMember> addList= new LinkedList<>();
            members.forEach(memberId ->{
                if(oldMemberIdResearchIdMap.containsKey(memberId)){
                    oldMemberIdResearchIdMap.remove(memberId);
                }else{
                    EduResearchMember temp = new EduResearchMember();
                    temp.setResearchId(id);
                    temp.setMemberId(memberId);
//                    temp.setMemberName(member.getName());
                    addList.add(temp);
                }
            });
            if(addList.size()>0){
                saveBatch(addList);
            }
            if(!oldMemberIdResearchIdMap.isEmpty()){
                baseMapper.deleteBatchIds(oldMemberIdResearchIdMap.values());
            }
        }else {
            EduResearchMemberService current = (EduResearchMemberService) AopContext.currentProxy();
            current.addResearchMembers(id, members);
        }



    }

    @Override
    public List<AvatarDTO> getFullMembersByResearchId(Long id, boolean latest) {

        LambdaQueryWrapper<EduResearchMember> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(EduResearchMember::getResearchId, id).select(EduResearchMember::getMemberId);
        wrapper.eq(EduResearchMember::getStatus, ResearchMemberStatus.APPROVED)
                .or(latest).eq(EduResearchMember::getStatus, ResearchMemberStatus.PENDING);

        List<EduResearchMember> researchMembers = baseMapper.selectList(wrapper);
        if(researchMembers.size()==0){
            return null;
        }
        List<Long> memberIds = researchMembers.stream().map(EduResearchMember::getMemberId).toList();
        LambdaQueryWrapper<EduMember> memberWrapper = Wrappers.lambdaQuery();
        memberWrapper.in(EduMember::getId, memberIds).select(EduMember::getId, EduMember::getAvatar, EduMember::getName);
        List<EduMember> members = memberService.list(memberWrapper);
        return members.stream().map(member -> AvatarDTO.builder()
                .id(member.getId()).
                avatar( member.getAvatar())
                .name(member.getName())
                .build()).collect(Collectors.toList());

    }
}
