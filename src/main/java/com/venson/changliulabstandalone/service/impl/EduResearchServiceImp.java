package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.entity.dto.*;
import com.venson.changliulabstandalone.entity.inter.ReviewAble;
import com.venson.changliulabstandalone.entity.pojo.EduMember;
import com.venson.changliulabstandalone.entity.pojo.EduReview;
import com.venson.changliulabstandalone.entity.vo.BasicMemberVo;
import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.exception.CustomizedException;
import com.venson.changliulabstandalone.service.admin.EduResearchMemberService;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.PageUtil;
import com.venson.changliulabstandalone.entity.pojo.EduResearch;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.mapper.EduResearchMapper;
import com.venson.changliulabstandalone.service.admin.EduResearchService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
public class EduResearchServiceImp extends ServiceImpl<EduResearchMapper, EduResearch> implements EduResearchService {

    private final EduResearchMemberService researchMemberService;


    @Override
    public PageResponse<EduResearch> getResearchPage(Integer page, Integer limit) {
        Page<EduResearch> researchPage = new Page<>(page, limit);
        LambdaQueryWrapper<EduResearch> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EduResearch::getId, EduResearch::getTitle, EduResearch::getReview, EduResearch::getIsModified, EduResearch::getIsPublished
                , EduResearch::getLanguage, EduResearch::getEnable);
        baseMapper.selectPage(researchPage, wrapper);
        return PageUtil.toBean(researchPage);
    }

    @Override
    @Transactional
    @CacheEvict(value = "research", allEntries = true)
    public Long addResearch(AdminResearchDTO research) {
        if (isDuplicatedTitle(research.getTitle(), null)) {
            throw new CustomizedException(200001, "Duplicated research Title");
        }
        EduResearch eduResearch = new EduResearch();
        BeanUtils.copyProperties(research, eduResearch);
        baseMapper.insert(eduResearch);
        researchMemberService.addResearchMembers(eduResearch.getId(), research.getMembers());
        return eduResearch.getId();
    }

    private boolean isDuplicatedTitle(String title,@Nullable Long excludeId) {
        LambdaQueryWrapper<EduResearch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduResearch::getTitle, title);
        wrapper.ne(ObjectUtils.isEmpty(excludeId), EduResearch::getId, excludeId);
        return baseMapper.exists(wrapper);
    }

    @Override
    @CacheEvict(value = "research", allEntries = true)
    public void updateResearch(Long id, AdminResearchDTO research) {
        EduResearch eduResearch = baseMapper.selectById(id);
        Assert.notNull(eduResearch, "No corresponding research");
        Assert.isTrue(id.equals(research.getId()) && id.equals(eduResearch.getId()), "Invalid Value");
        Assert.isTrue(eduResearch.getReview() != ReviewStatus.APPLIED, "Research is under review");
        Assert.isTrue(isDuplicatedTitle(research.getTitle(), research.getId()), "Title already used");
        BeanUtils.copyProperties(research, eduResearch);
        baseMapper.updateById(eduResearch);
        researchMemberService.updateResearchMembers(id, research.getMembers());

    }

    @Override
    public ResearchDTO getResearchPreviewById(Long id) {
        EduResearch eduResearch = baseMapper.selectById(id);
        ResearchDTO view = new ResearchDTO();
        view.setHtmlBrBase64(eduResearch.getHtmlBrBase64());
        view.setPublishedHtmlBrBase64(eduResearch.getPublishedHtmlBrBase64());
        view.setTitle(eduResearch.getTitle());
        view.setLanguage(eduResearch.getLanguage());
        view.setReview(eduResearch.getReview());
        view.setEnable(eduResearch.getEnable());
        view.setIsModified(eduResearch.getIsModified());
        return view;

    }

    @Override
    public PageResponse<EduResearch> getResearchReviewPage(PageQueryVo vo) {
        LambdaQueryWrapper<EduResearch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduResearch::getReview, ReviewStatus.APPLIED)
                .select(EduResearch::getId, EduResearch::getTitle, EduResearch::getReview, EduResearch::getIsModified
                        , EduResearch::getLanguage, EduResearch::getEnable);
        Page<EduResearch> page = new Page<>(vo.page(), vo.perPage());
        baseMapper.selectPage(page, wrapper);

        return PageUtil.toBean(page);
    }

    @Override
    @Transactional
    public void switchEnableById(Long id) {
        EduResearch eduResearch = baseMapper.selectByIdOp(id);
        eduResearch.setEnable(!eduResearch.getEnable());
        baseMapper.updateById(eduResearch);

    }

    @Override
    @Transactional
    public void removeResearchById(Long id) {
        EduResearch research = baseMapper.selectByIdOp(id);
        research.setIsRemoveAfterReview(!research.getIsRemoveAfterReview());
        baseMapper.updateById(research);
    }

    @Override
    public AdminResearchDTO getResearchById(Long id) {
        EduResearch research = baseMapper.selectById(id);
        List<BasicMemberVo> members = researchMemberService.getMembersByResearchId(id);
        AdminResearchDTO dto = new AdminResearchDTO();
        BeanUtils.copyProperties(research, dto, "htmlBrBase64");
        dto.setMembers(members);
        return dto;
    }

    @Override
    public PageResponse<EduResearch> getResearchPage(PageQueryVo pageQueryVo) {
        LambdaQueryWrapper<EduResearch> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EduResearch::getId, EduResearch::getTitle, EduResearch::getReview, EduResearch::getIsModified
                , EduResearch::getIsPublished,EduResearch::getIsRemoveAfterReview
                , EduResearch::getLanguage, EduResearch::getEnable);
        Page<EduResearch> page = new Page<>(pageQueryVo.page(), pageQueryVo.perPage());
        baseMapper.selectPage(page, wrapper);
        return PageUtil.toBean(page);
    }

    @Override
    public List<ReviewBasicDTO> getInfoByReviews(List<EduReview> reviews) {
        if(reviews.isEmpty()){
            return Collections.emptyList();
        }
        List<Long> researchIds= reviews.stream().map(EduReview::getRefId).collect(Collectors.toList());
        Map<Long, EduResearch> researchMap = baseMapper.selectBatchIds(researchIds).stream().collect(Collectors.toMap(EduResearch::getId, Function.identity()));
        return reviews.stream().map(review->ReviewBasicDTO.builder()
                .id(review.getId())
                .review(review.getStatus())
                .title(researchMap.get(review.getRefId()).getTitle())
                .gmtCreate(review.getGmtCreate())
                .refId(review.getRefId())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public ReviewAble getReviewById(Long id) {

        EduResearch eduResearch = baseMapper.selectById(id);
        ShowResearchDTO applied= new ShowResearchDTO();
        applied.setHtml(eduResearch.getHtmlBrBase64());
        applied.setTitle(eduResearch.getTitle());
        List<EduMember> members = researchMemberService.getFullMembersByResearchId(id);
        applied.setMembers(members);
        return applied;
    }
}
