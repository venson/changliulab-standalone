package com.venson.changliulab.service.front.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.pojo.EduCoursePublished;
import com.venson.changliulab.entity.pojo.EduScholar;
import com.venson.changliulab.entity.front.dto.MemberFrontDTO;
import com.venson.changliulab.entity.front.dto.ResearchFrontDTO;
import com.venson.changliulab.service.front.CourseFrontService;
import com.venson.changliulab.service.front.ResearchFrontService;
import com.venson.changliulab.service.front.ScholarFrontService;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.PageUtil;
import com.venson.changliulab.entity.pojo.EduMember;
import com.venson.changliulab.entity.enums.MemberLevel;
import com.venson.changliulab.entity.front.dto.MemberFrontBriefDTO;
import com.venson.changliulab.service.admin.EduMemberService;
import com.venson.changliulab.service.front.MemberFrontService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberFrontServiceImpl implements MemberFrontService {
    @Autowired
    private EduMemberService memberService;
    @Autowired
    private ResearchFrontService researchFrontService;
    @Autowired
    private ScholarFrontService scholarFrontService;
    @Autowired
    private CourseFrontService courseFrontService;
//    @Override
//    @Cacheable(value = "member:PI")
//    public List<EduMember> getPIMemberFront() {
//        LambdaQueryWrapper<EduMember> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(EduMember::getLevel, MemberLevel.PI)
//                .select(EduMember::getId,EduMember::getAvatar,
//                        EduMember::getName,EduMember::getIntro,
//                        EduMember::getTitle,EduMember::getCareer);
//        return memberService.list(wrapper);
//    }

    @Override
    @Cacheable(value = FrontCacheConst.MEMBER_PAGE_NAME, key = "#level+':'+#page")
    public PageResponse<EduMember> getPageFrontMemberListByLevel(Integer page, Integer limit, MemberLevel level) {
        LambdaQueryWrapper<EduMember> wrapper = new LambdaQueryWrapper<>();
//        switch (level) {
//            case "PI" -> wrapper.eq(EduMember::getLevel, MemberLevel.PI);
//            case "CM" -> wrapper.eq(EduMember::getLevel, MemberLevel.CURRENT_MEMBER);
//            case "FM" -> wrapper.eq(EduMember::getLevel, MemberLevel.FORMER_MEMBER);
//            case "IN" -> wrapper.eq(EduMember::getLevel, MemberLevel.INTERN);
//            default -> throw new CustomizedException(20001, "invalid level");
//        }
        wrapper.eq(level!=null, EduMember::getLevel, level);
        wrapper.select(EduMember::getId,EduMember::getAvatar,
                        EduMember::getName,EduMember::getIntro,
                        EduMember::getTitle,EduMember::getCareer);
        Page<EduMember> memberPage = new Page<>(page,limit);
        memberService.page(memberPage,wrapper);
        return PageUtil.toBean(memberPage);
    }

    @Override
    @Cacheable(value = FrontCacheConst.MEMBER_NAME, key = FrontCacheConst.MEMBER_DTO_KEY_PREFIX + "+#id + ':'+#sPage+':'+#cPage")
    public MemberFrontDTO getMemberFrontDTOByID(Long id, Integer sPage, Integer cPage, Integer sSize, Integer cSize) {
        MemberFrontBriefDTO member = memberService.getMemberFrontById(id);
        if(member!=null){
            PageResponse<EduScholar> scholars = scholarFrontService.getPageScholarByMemberId(id, sPage, sSize);
            PageResponse<EduCoursePublished> courses = courseFrontService.getPageCourseByMemberId(id, cPage, cSize);
            List<ResearchFrontDTO> researches = researchFrontService.getResearchListByMemberId(id);
            MemberFrontDTO dto = new MemberFrontDTO();
            dto.setScholars(scholars);
            dto.setCourses(courses);
            dto.setMember(member);
            dto.setResearches(researches);
            return dto;
        }

        return null;
    }

    @Override
    @Cacheable(value = FrontCacheConst.HOME_NAME,key = FrontCacheConst.HOME_MEMBER_KEY)
    public List<MemberFrontBriefDTO> getFrontIndexMember() {
        return memberService.getFrontIndexMember();
//        return memberService.getBas;
    }

}
