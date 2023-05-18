package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.pojo.EduResearchMember;
import com.venson.changliulabstandalone.entity.vo.BasicMemberVo;
import com.venson.changliulabstandalone.mapper.EduResearchMemberMapper;
import com.venson.changliulabstandalone.service.admin.EduMemberService;
import com.venson.changliulabstandalone.service.admin.EduResearchMemberService;
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

    @Override
    public List<BasicMemberVo> getMembersByResearchId(Long id) {

        LambdaQueryWrapper<EduResearchMember> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(EduResearchMember::getResearchId, id).select(EduResearchMember::getMemberId, EduResearchMember::getMemberName);

        List<EduResearchMember> researchMembers = baseMapper.selectList(wrapper);
        LinkedList<BasicMemberVo> members = new LinkedList<>();
        if(researchMembers.size()>0){
            researchMembers.forEach(member -> {
                BasicMemberVo temp = new BasicMemberVo(member.getMemberId(), member.getMemberName());
                members.add(temp);
            });
        }
        return members;
    }

    @Transactional
    @Override
    public void addResearchMembers(Long researchId, List<BasicMemberVo> members) {
        if( members.size() > 0){
            LinkedList<EduResearchMember> researchMembers = new LinkedList<>();
            members.forEach(member ->{
                EduResearchMember temp = new EduResearchMember();
                temp.setResearchId(researchId);
                temp.setMemberId(temp.getMemberId());
                temp.setMemberName(temp.getMemberName());
                researchMembers.add(temp);
            });
            saveOrUpdateBatch(researchMembers);
        }

    }

    @Override
    @Transactional
    public void updateResearchMembers(Long id, List<BasicMemberVo> members) {
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
            members.forEach(member ->{
                if(oldMemberIdResearchIdMap.containsKey(member.getId())){
                    oldMemberIdResearchIdMap.remove(member.getId());
                }else{
                    EduResearchMember temp = new EduResearchMember();
                    temp.setResearchId(id);
                    temp.setMemberId(member.getId());
                    temp.setMemberName(member.getName());
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
    public List<EduMember> getFullMembersByResearchId(Long id) {

        LambdaQueryWrapper<EduResearchMember> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(EduResearchMember::getResearchId, id).select(EduResearchMember::getMemberId);

        List<EduResearchMember> researchMembers = baseMapper.selectList(wrapper);
        List<Long> memberIds = researchMembers.stream().map(EduResearchMember::getMemberId).toList();
        LambdaQueryWrapper<EduMember> memberWrapper = Wrappers.lambdaQuery();
        memberWrapper.in(EduMember::getId, memberIds).select(EduMember::getId, EduMember::getAvatar, EduMember::getName);
        return memberService.list(memberWrapper);
    }
}
