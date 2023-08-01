package com.venson.changliulab.service.front.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.PageUtil;
import com.venson.changliulab.entity.pojo.EduMember;
import com.venson.changliulab.entity.pojo.EduMemberScholar;
import com.venson.changliulab.entity.pojo.EduScholar;
import com.venson.changliulab.entity.pojo.EduScholarCitation;
import com.venson.changliulab.entity.front.dto.ScholarFrontDTO;
import com.venson.changliulab.entity.front.vo.CitationFrontVo;
import com.venson.changliulab.entity.front.vo.ScholarFrontFilterVo;
import com.venson.changliulab.service.admin.EduMemberScholarService;
import com.venson.changliulab.service.admin.EduMemberService;
import com.venson.changliulab.service.admin.EduScholarCitationService;
import com.venson.changliulab.service.admin.EduScholarService;
import com.venson.changliulab.service.front.ScholarFrontService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ScholarFrontServiceImpl implements ScholarFrontService {
    @Autowired
    private EduMemberScholarService memberScholarService;

    @Autowired
    private EduScholarService scholarService;

    @Autowired
    private EduScholarCitationService scholarCitationService;

    @Autowired
    private EduMemberService memberService;

    @Autowired
    @Lazy
    private ScholarFrontService self;


    @Override
    @Cacheable(value = FrontCacheConst.SCHOLAR_MEMBER_PAGE_NAME,key = "#memberId +':'+ #pageNum ")
    public PageResponse<EduScholar> getPageScholarByMemberId(Long memberId, Integer pageNum, Integer limit) {
        Page<EduScholar> page = new Page<>(pageNum, limit);
        List<Long> scholarIdList = memberScholarService.getScholarIdListByMemberId(memberId);
        LambdaQueryWrapper<EduScholar> wrapper = new LambdaQueryWrapper<>();
        if(scholarIdList.size()==0){
            return null;
        }
        wrapper.in(EduScholar::getId,scholarIdList);
        scholarService.page(page,wrapper);
        return PageUtil.toBean(page);
    }

    @Override
    @Cacheable(value = FrontCacheConst.SCHOLAR_NAME, key = "#scholarId")
    public ScholarFrontDTO getScholarDTOById(String scholarId) {
        LambdaQueryWrapper<EduScholarCitation> citationWrapper = new LambdaQueryWrapper<>();
        citationWrapper.eq(EduScholarCitation::getScholarId, scholarId)
                .select(EduScholarCitation::getYear,EduScholarCitation::getCitations);
        List<EduScholarCitation> citations =scholarCitationService.list(citationWrapper);
        LambdaQueryWrapper<EduMemberScholar> memberWrapper = new LambdaQueryWrapper<>();
        memberWrapper.eq(EduMemberScholar::getScholarId, scholarId).select(EduMemberScholar::getMemberId);
        List<EduMemberScholar> memberList = memberScholarService.list(memberWrapper);
        List<Long> memberIdList = memberList.stream().map(EduMemberScholar::getMemberId).collect(Collectors.toList());
        List<EduMember> members = memberService.listByIds(memberIdList);
        EduScholar scholar = scholarService.getById(scholarId);
        return new ScholarFrontDTO(scholar,members,citations);
    }

    @Override
    @Cacheable(value = FrontCacheConst.SCHOLAR_PAGE_NAME)
    public PageResponse<EduScholar> getPageScholarWithFilter(Integer pageNum, Integer limit, ScholarFrontFilterVo filterVo) {
        PageResponse<EduScholar> pageRes;
        if(ObjectUtils.isEmpty(filterVo)){
            pageRes= self.doGetPageScholar(pageNum,limit);

        }else{
            pageRes = doGetPageScholarWithFilter(pageNum,limit, filterVo);

        }
        return pageRes;
    }

    @Override
    public PageResponse<EduScholar> doGetPageScholar(Integer pageNum, Integer limit) {
        log.info("no cache");
        Page<EduScholar> page = new Page<>(pageNum,limit);
        scholarService.page(page);
        return PageUtil.toBean(page);
    }

    @Override
    @Cacheable(value = FrontCacheConst.SCHOLAR_NAME,key = FrontCacheConst.SCHOLAR_CITATION_KEY + "+#memberId")
    public CitationFrontVo getCitationByMemberId(Long memberId) {
        List<Long> scholarIds = memberScholarService.getScholarIdListByMemberId(memberId);
        LambdaQueryWrapper<EduScholarCitation> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(EduScholarCitation::getScholarId,scholarIds)
                .select(EduScholarCitation::getCitations,EduScholarCitation::getYear)
                .orderByAsc(EduScholarCitation::getYear);
        List<EduScholarCitation> scholarCitation = scholarCitationService.list(wrapper);
        return getCitationFrontVoFromList(scholarCitation);
    }


    /**
     * change EduScholarCitation list to CitationFrontVO
     * @param scholarCitation list of EduScholarCitation
     * @return CitationFrontVO
     */
    private CitationFrontVo getCitationFrontVoFromList(List<EduScholarCitation> scholarCitation) {
        LinkedHashMap<Integer, Integer> map = scholarCitation.stream()
                .collect(Collectors.groupingBy(EduScholarCitation::getYear, LinkedHashMap::new,
                        Collectors.summingInt(EduScholarCitation::getCitations)));

        List<Integer> dataX = new ArrayList<>();
        List<Integer> dataY = new ArrayList<>();
        map.forEach((year,citation)->{
            dataX.add(year);
            dataY.add(citation);
        });
        return new CitationFrontVo(dataX, dataY);
    }

    private PageResponse<EduScholar> doGetPageScholarWithFilter(Integer pageNum, Integer limit, ScholarFrontFilterVo filterVo) {
        Page<EduScholar> page = new Page<>(pageNum,limit);
        LambdaQueryWrapper<EduScholar> wrapper = new LambdaQueryWrapper<>();
        String authors = filterVo.getAuthors();
        String year= filterVo.getYear();
        String title = filterVo.getTitle();
        wrapper.like(StringUtils.hasText(authors),EduScholar::getAuthors,authors)
                .like(StringUtils.hasText(year),EduScholar::getYear,year)
                .like(StringUtils.hasText(title),EduScholar::getTitle,title);
//        wrapper.select(EduScholar.class,i->i.getFieldFill()== FieldFill.DEFAULT && !i.isLogicDelete() && !i.isVersion());
        wrapper.select(EduScholar::getId,EduScholar::getTitle,EduScholar::getAuthors,
                EduScholar::getPublisher,EduScholar::getVolume,EduScholar::getIssue,
                EduScholar::getPages,EduScholar::getTotalCitations,EduScholar::getYear);
        scholarService.page(page,wrapper);
        return PageUtil.toBean(page);

    }



}
