package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.dto.AvatarDTO;
import com.venson.changliulab.entity.dto.MemberQuery;
import com.venson.changliulab.entity.enums.MemberLevel;
import com.venson.changliulab.entity.front.dto.MemberFrontBriefDTO;
import com.venson.changliulab.entity.pojo.EduMember;
import com.venson.changliulab.entity.vo.MemberVo;
import com.venson.changliulab.entity.vo.admin.AdminMemberVo;
import com.venson.changliulab.entity.vo.admin.ListQueryParams;
import com.venson.changliulab.mapper.EduMemberMapper;
import com.venson.changliulab.service.admin.EduMemberService;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2022-05-02
 */
@Service
public class EduMemberServiceImp extends ServiceImpl<EduMemberMapper, EduMember> implements EduMemberService {

    @Override
    public PageResponse<EduMember> getMemberPage(Integer pageNum, Integer limit, MemberQuery memberQuery) {

        Page<EduMember> pageMember = new Page<>(pageNum,limit);
        LambdaQueryWrapper<EduMember> wrapper = new QueryWrapper<EduMember>().lambda();
        if(memberQuery!=null){
            String name = memberQuery.getName();
            MemberLevel level = memberQuery.getLevel();
            String begin = memberQuery.getBegin();
            String end = memberQuery.getEnd();
            if(!ObjectUtils.isEmpty(name)){
                wrapper.like(EduMember::getName,name);
            }
            if(!ObjectUtils.isEmpty(level)){
                wrapper.eq(EduMember::getLevel,level.getValue());
            }
            if(!ObjectUtils.isEmpty(begin)){
                wrapper.ge(EduMember::getGmtCreate, begin);
            }
            if(!ObjectUtils.isEmpty(end)){
                wrapper.le(EduMember::getGmtCreate, end);
            }
        }

        wrapper.orderByDesc(EduMember::getId);
        baseMapper.selectPage(pageMember,wrapper);
        return PageUtil.toBean(pageMember);

    }

    @Override
    @CacheEvict(value = {FrontCacheConst.MEMBER_PAGE_NAME, FrontCacheConst.MEMBER_NAME}, allEntries = true)
    public void updateMember(Long id, MemberVo member) {
        EduMember eduMember =baseMapper.selectById(id);
        BeanUtils.copyProperties(member, eduMember);
        baseMapper.updateById(eduMember);
    }

    @Override
    public List<MemberFrontBriefDTO> getFrontIndexMember() {
        return baseMapper.getFrontIndexMember();
    }

    @Override
    public List<EduMember> getCurrentMember() {
        LambdaQueryWrapper<EduMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EduMember::getId,EduMember::getName, EduMember::getLevel);
        wrapper.ne(EduMember::getLevel, MemberLevel.FORMER_MEMBER).ne(EduMember::getLevel,MemberLevel.TECH);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<EduMember> getAllMember() {
        LambdaQueryWrapper<EduMember> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EduMember::getId,EduMember::getName, EduMember::getLevel);
        wrapper.ne(EduMember::getLevel,MemberLevel.TECH);
        return baseMapper.selectList(wrapper);
    }

    @Override
    @Cacheable(value = FrontCacheConst.MEMBER_NAME ,key = FrontCacheConst.MEMBER_KEY_PREFIX + "+#id")
    public MemberFrontBriefDTO getMemberFrontById(Long id) {

        EduMember eduMember = baseMapper.selectById(id);
        if(eduMember!= null){
            MemberFrontBriefDTO member = new MemberFrontBriefDTO();
            BeanUtils.copyProperties(eduMember,member);
            return member;
        }
        return null;
    }

    @Override
    public Long addMember(AdminMemberVo eduMember) {
        EduMember member = new EduMember();
        BeanUtils.copyProperties(eduMember,member);
        baseMapper.insert(member);
        return member.getId();
    }

    @Override
    public PageResponse<EduMember> getMemberPage(ListQueryParams params) {
        List<HashMap<String, String>> filters = params.getFilter();
        String member = filters.get(0).getOrDefault("member","all");

        Page<EduMember> pageMember = new Page<>(params.page(), params.perPage());
        LambdaQueryWrapper<EduMember> wrapper = new QueryWrapper<EduMember>().lambda();
        if(member.equals("all")){
            wrapper.select(EduMember::getId, EduMember::getName,EduMember::getTitle,EduMember::getLevel, EduMember::getAvatar);

        }else{
            wrapper.select(EduMember::getId, EduMember::getName);
            wrapper.ne(EduMember::getLevel, MemberLevel.TECH);
            wrapper.ne(EduMember::getLevel, MemberLevel.FORMER_MEMBER);

        }
        wrapper.orderByDesc(EduMember::getId);
        baseMapper.selectPage(pageMember,wrapper);
        return PageUtil.toBean(pageMember);
    }

    @Override
    public EduMember getMemberById(Long id) {
        EduMember eduMember = baseMapper.selectById(id);
        if(eduMember == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return eduMember;
    }

    @Override
    public AvatarDTO getMemberAvatarById(long id) {
        EduMember eduMember = baseMapper.selectById(id);
        if(eduMember==null) return null;
        return AvatarDTO.builder()
                .avatar(eduMember.getAvatar())
                .id(eduMember.getId())
                .name(eduMember.getName())
                .build();
    }
}
