package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.vo.admin.ListQueryParams;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.PageUtil;
import com.venson.changliulabstandalone.entity.pojo.EduScholar;
import com.venson.changliulabstandalone.entity.pojo.EduScholarCitation;
import com.venson.changliulabstandalone.entity.dto.ScholarAdminDTO;
import com.venson.changliulabstandalone.entity.vo.ScholarFilterVo;
import com.venson.changliulabstandalone.mapper.EduScholarMapper;
import com.venson.changliulabstandalone.service.admin.EduMemberScholarService;
import com.venson.changliulabstandalone.service.admin.EduScholarCitationService;
import com.venson.changliulabstandalone.service.admin.EduScholarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-06-18
 */
@Service
public class EduScholarServiceImp extends ServiceImpl<EduScholarMapper, EduScholar> implements EduScholarService {
    @Autowired
    private EduMemberScholarService memberScholarService;

    @Autowired
    private EduScholarCitationService scholarCitationService;


    @Override
    public PageResponse<EduScholar> getPageScholar(Integer page, Integer limit, ScholarFilterVo filterVo) {
        Page<EduScholar> pageScholar = new Page<>(page,limit);
        LambdaQueryWrapper<EduScholar> wrapper = new LambdaQueryWrapper<>();
        if(!ObjectUtils.isEmpty(filterVo)){
            wrapper.eq(StringUtils.hasText(filterVo.getYear()),EduScholar::getYear, filterVo.getYear());
            wrapper.like(StringUtils.hasText(filterVo.getAuthors()),EduScholar::getAuthors, filterVo.getAuthors());
            wrapper.like(StringUtils.hasText(filterVo.getTitle()),EduScholar::getTitle, filterVo.getTitle());
        }
        baseMapper.selectPage(pageScholar,wrapper);

        return PageUtil.toBean(pageScholar);
    }

    @Override
    public ScholarAdminDTO getScholarById(Long id) {
        EduScholar scholar = baseMapper.selectById(id);
        List<Long> memberList = memberScholarService.getMemberIdsByScholarId(id);
        List<EduScholarCitation>  citationList = scholarCitationService.getCitationsByScholarId(id);

        ScholarAdminDTO scholarAdminDTO = new ScholarAdminDTO();
        BeanUtils.copyProperties(scholar, scholarAdminDTO);
        scholarAdminDTO.setMemberIdList(memberList);
        scholarAdminDTO.setCitationList(citationList);
        return scholarAdminDTO;
    }

    @Override
    @Caching(evict = {@CacheEvict(value = FrontCacheConst.SCHOLAR_NAME, key="#scholar.id"), @CacheEvict(value = FrontCacheConst.SCHOLAR_PAGE_NAME, allEntries = true)})
    public void updateScholar(ScholarAdminDTO scholar) {
        Long scholarId = scholar.getId();
        EduScholar eduScholar = baseMapper.selectById(scholarId);
        Assert.notNull(eduScholar,"Scholar Not exist");
        BeanUtils.copyProperties(scholar, eduScholar);
        baseMapper.updateById(eduScholar);
        scholarCitationService.updateScholarCitation(scholar.getCitationList(),scholarId);
        memberScholarService.updateMemberScholarByMemberIdList(scholarId,scholar.getMemberIdList(),scholar.getMemberList());

    }

    @Override
    public Long addScholar(ScholarAdminDTO scholar) {
        EduScholar eduScholar = new EduScholar();
        BeanUtils.copyProperties(scholar,eduScholar);
        baseMapper.insert(eduScholar);
        Long id = eduScholar.getId();
        scholarCitationService.addScholarCitation(scholar.getCitationList(),id);
        memberScholarService.addMemberScholarByMemberIdList(id,scholar.getMemberList());

        return null;
    }

    @Override
    public PageResponse<EduScholar> getPageScholar(ListQueryParams queryParams) {

        Page<EduScholar> pageScholar = new Page<>(queryParams.page(), queryParams.perPage());
        LambdaQueryWrapper<EduScholar> wrapper = new LambdaQueryWrapper<>();
        List<HashMap<String, String>> filter = queryParams.getFilter();
        String title = filter.get(0).getOrDefault("title", "");
//            wrapper.eq(StringUtils.hasText(filterVo.getYear()),EduScholar::getYear, filterVo.getYear());
//            wrapper.like(StringUtils.hasText(filterVo.getAuthors()),EduScholar::getAuthors, filterVo.getAuthors());
            wrapper.like(StringUtils.hasText(title),EduScholar::getTitle, title);
            wrapper.select(EduScholar::getId, EduScholar::getTitle, EduScholar::getYear, EduScholar::getTotalCitations);
        baseMapper.selectPage(pageScholar,wrapper);

        return PageUtil.toBean(pageScholar);
    }

}
