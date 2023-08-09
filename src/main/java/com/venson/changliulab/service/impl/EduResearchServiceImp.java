package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.entity.dto.*;
import com.venson.changliulab.entity.enums.ResearchMemberStatus;
import com.venson.changliulab.entity.enums.ReviewStatus;
import com.venson.changliulab.entity.inter.ReviewAble;
import com.venson.changliulab.entity.pojo.EduResearch;
import com.venson.changliulab.entity.pojo.EduResearchMember;
import com.venson.changliulab.entity.pojo.EduResearchPublished;
import com.venson.changliulab.entity.pojo.EduReview;
import com.venson.changliulab.entity.vo.admin.CommonMetaVo;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.exception.CustomizedException;
import com.venson.changliulab.mapper.EduResearchMapper;
import com.venson.changliulab.service.admin.EduResearchMemberService;
import com.venson.changliulab.service.admin.EduResearchPublishedService;
import com.venson.changliulab.service.admin.EduResearchService;
import com.venson.changliulab.service.admin.ReviewableService;
import com.venson.changliulab.utils.Assert;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.PageUtil;
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
public class EduResearchServiceImp extends ServiceImpl<EduResearchMapper, EduResearch> implements EduResearchService , ReviewableService {

    private final EduResearchMemberService researchMemberService;
    private final EduResearchPublishedService researchPublishedService;


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
        researchMemberService.addResearchMembers(eduResearch.getId(), research.getMemberIds());
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
        researchMemberService.updateResearchMembers(id, research.getMemberIds());

    }

    @Override
    public ResearchPreviewDTO getResearchPreviewById(Long id) {
        EduResearch eduResearch = baseMapper.selectById(id);
        List<AvatarDTO> members = researchMemberService.getFullMembersByResearchId(id, true);
        return ResearchPreviewDTO.builder()
                .id(eduResearch.getId())
                .title(eduResearch.getTitle())
                .htmlBrBase64(eduResearch.getHtmlBrBase64())
                .enable(eduResearch.getEnable())
                .review(eduResearch.getReview())
                .isModified(eduResearch.getIsModified())
                .members(members)
                .isPublished(eduResearch.getIsPublished())
                .build();

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
    public AdminResearchDTO getResearchById(Long id, CommonMetaVo vo) {

        LambdaQueryWrapper<EduResearch> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(EduResearch::getId, id);

        if (vo !=null && "preview".equals(vo.type())){
            wrapper.select(EduResearch::getId, EduResearch::getHtmlBrBase64,
                    EduResearch::getIsModified, EduResearch::getTitle,
                    EduResearch::getReview, EduResearch::getIsRemoveAfterReview);
        }else{
            wrapper.select(EduResearch::getId,EduResearch::getIsModified,
                    EduResearch::getMarkdown,EduResearch::getTitle,
                    EduResearch::getReview,
                    EduResearch::getIsRemoveAfterReview);
        }

        EduResearch research = baseMapper.selectOne(wrapper);
//        List<BasicMemberVo> members = researchMemberService.getMembersByResearchId(id);
        List<AvatarDTO> members = researchMemberService.getFullMembersByResearchId(id, true);
        AdminResearchDTO dto = new AdminResearchDTO();
        BeanUtils.copyProperties(research, dto);
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
                .type(review.getRefType())
                .gmtCreate(review.getGmtCreate())
                .refId(review.getRefId())
                .build()
        ).collect(Collectors.toList());
    }

    @Override
    public ReviewAble getReviewById(Long id) {

        EduResearch eduResearch = baseMapper.selectById(id);
        Assert.notNull(eduResearch,"Research Not Found");
        ShowResearchDTO applied= new ShowResearchDTO();
        applied.setHtml(eduResearch.getHtmlBrBase64());
        applied.setTitle(eduResearch.getTitle());
        List<AvatarDTO> members = researchMemberService.getFullMembersByResearchId(id,true);
        applied.setMembers(members);
        return applied;
    }

    @Override
    public void updateReviewedResearch(List<Long> ids, ReviewStatus reviewStatus) {
        LambdaUpdateWrapper<EduResearch> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(EduResearch::getReview,reviewStatus)
                .in(EduResearch::getId,ids);
        baseMapper.update(null, wrapper);

    }

    @Override
    public boolean existsByIds(List<Long> ids, ReviewStatus... reviewStatus) {
        if (ids.isEmpty() || reviewStatus.length == 0){
            return false;
        }
        LambdaQueryWrapper<EduResearch> wrapper = Wrappers.lambdaQuery();
        int count = baseMapper.selectCount(wrapper).intValue();

        return count == ids.size();
    }

    @Override
    public void publishReviewedResearch(List<Long> ids) {
        LambdaQueryWrapper<EduResearch> wrapper = Wrappers.lambdaQuery();
        wrapper.in(EduResearch::getId, ids)
                        .eq(EduResearch::getReview, ReviewStatus.APPLIED);
        List<EduResearch> researches = baseMapper.selectList(wrapper);
        if(researches.isEmpty()){
            return;
        }
        List<Long> researchIds = researches.stream().map(EduResearch::getId).collect(Collectors.toList());
        List<EduResearchPublished> publishedList = researches.stream().map(research->{
            EduResearchPublished temp = new EduResearchPublished();
            BeanUtils.copyProperties(research,temp);
            return temp;
                }).collect(Collectors.toList());

        researchPublishedService.saveBatch(publishedList);
        LambdaUpdateWrapper<EduResearchMember> memberWrapper = Wrappers.lambdaUpdate();
        memberWrapper.in(EduResearchMember::getResearchId,researchIds)
                .eq(EduResearchMember::getStatus, ResearchMemberStatus.PENDING)
                .set(EduResearchMember::getStatus, ResearchMemberStatus.APPROVED);
        researchMemberService.update(null,memberWrapper);
    }

//    @Override
//    public ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id) {
//    }

    @Override
    public ReviewTargetDTO<ReviewAble> getReviewByTargetId(Long id) {
        EduResearch eduResearch = baseMapper.selectById(id);
        Assert.notNull(eduResearch,"Research Not Found");
        List<AvatarDTO> latestMembers = researchMemberService.getFullMembersByResearchId(id,true);
        List<AvatarDTO> members = researchMemberService.getFullMembersByResearchId(id, false);
        EduResearchPublished researchPublished = researchPublishedService.getById(id);
        ShowResearchDTO applied= ShowResearchDTO.builder()
                .html(eduResearch.getHtmlBrBase64())
                .members(members)
                .title(eduResearch.getTitle()).build();
        ShowResearchDTO reviewed=null;
        if(researchPublished!=null){
            reviewed = ShowResearchDTO.builder()
                    .html(researchPublished.getHtmlBrBase64())
                    .members(latestMembers)
                    .title(researchPublished.getTitle()).build();

        }



        return ReviewTargetDTO.<ReviewAble>builder()
                .id(id)
                .applied(applied)
                .reviewed(reviewed)
                .status(eduResearch.getReview())
                .title(eduResearch.getTitle())
                .build();
    }
}
