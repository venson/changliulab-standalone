package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.entity.dto.AdminResearchDTO;
import com.venson.changliulabstandalone.entity.vo.BasicMemberVo;
import com.venson.changliulabstandalone.exception.CustomizedException;
import com.venson.changliulabstandalone.service.admin.EduResearchMemberService;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.PageUtil;
import com.venson.changliulabstandalone.entity.pojo.EduResearch;
import com.venson.changliulabstandalone.entity.dto.ResearchDTO;
import com.venson.changliulabstandalone.entity.enums.LanguageEnum;
import com.venson.changliulabstandalone.entity.enums.ReviewStatus;
import com.venson.changliulabstandalone.mapper.EduResearchMapper;
import com.venson.changliulabstandalone.service.admin.EduResearchService;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

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
public class EduResearchServiceImp extends ServiceImpl<EduResearchMapper, EduResearch> implements EduResearchService {
    @Autowired
    EduResearchMemberService researchMemberService;

//    @Override
//    public List<EduResearch> getResearchReviewList() {
//        LambdaQueryWrapper<EduResearch> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(EduResearch::getReview, ReviewStatus.APPLIED);
//        return baseMapper.selectList(wrapper);
//    }

    @Override
    public PageResponse<EduResearch> getResearchPage(Integer page, Integer limit) {
        Page<EduResearch> researchPage = new Page<>(page, limit);
        LambdaQueryWrapper<EduResearch> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(EduResearch::getId, EduResearch::getTitle, EduResearch::getReview, EduResearch::getIsModified,EduResearch::getIsPublished
                , EduResearch::getLanguage, EduResearch::getEnable);
        baseMapper.selectPage(researchPage, wrapper);
        return PageUtil.toBean(researchPage);
    }

    @Override
    @Transactional
    @CacheEvict(value = "research",allEntries = true)
    public Long addResearch(AdminResearchDTO research) {
        if (isDuplicatedTitle(research.getTitle(), null)) {
            throw new CustomizedException(200001, "Duplicated research Title");
        }
        EduResearch eduResearch = new EduResearch();
        BeanUtils.copyProperties(research,eduResearch);
        baseMapper.insert(eduResearch);
        researchMemberService.addResearchMembers(eduResearch.getId(), research.getMembers());
        return eduResearch.getId();
    }

    private boolean isDuplicatedTitle(String title, Long excludeId) {
        LambdaQueryWrapper<EduResearch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduResearch::getTitle, title);
        if(!ObjectUtils.isEmpty(excludeId)){
                wrapper.ne(EduResearch::getId, excludeId);
        }
        EduResearch duplicatedTitle = baseMapper.selectOne(wrapper);
        return duplicatedTitle != null;
    }
    private boolean checkUsableTitle(String title, @Nullable Long excludeId) {
        LambdaQueryWrapper<EduResearch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduResearch::getTitle, title);
        if(excludeId!=null){
            wrapper.ne(EduResearch::getId, excludeId);
        }
        return !baseMapper.exists(wrapper);
    }

    @Override
    @CacheEvict(value = "research",allEntries = true)
    public void updateResearch(Long id, AdminResearchDTO research) {
        EduResearch eduResearch = baseMapper.selectById(id);
        Assert.notNull(eduResearch, "No corresponding research");
        Assert.isTrue(id.equals(research.getId()) && id.equals(eduResearch.getId()), "Invalid Value");
        Assert.isTrue(eduResearch.getReview() != ReviewStatus.APPLIED, "Research is under review");
        Assert.isTrue(checkUsableTitle(research.getTitle(),research.getId()), "Title already used");
        BeanUtils.copyProperties(research,eduResearch);
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
    public PageResponse<EduResearch> getResearchReviewPage(Integer current, Integer size) {
        LambdaQueryWrapper<EduResearch> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(EduResearch::getReview, ReviewStatus.APPLIED)
                .select(EduResearch::getId, EduResearch::getTitle, EduResearch::getReview, EduResearch::getIsModified
                , EduResearch::getLanguage, EduResearch::getEnable);
        Page<EduResearch> page = new Page<>(current, size);
        baseMapper.selectPage(page,wrapper);

        return PageUtil.toBean(page);
    }

    @Override
    @Transactional
    public void switchEnableById(Long id, LanguageEnum lang) {
        EduResearch eduResearch = baseMapper.selectById(id);
////        if(!eduResearch.getEnable()){
//            LambdaUpdateWrapper<EduResearch> wrapper = Wrappers.lambdaUpdate();
//            wrapper.eq(EduResearch::getId, id).eq(EduResearch::getIsPublished,true)
////                    .eq(EduResearch::getLanguage, lang)
//                    .set(EduResearch::getEnable, );
//            baseMapper.update(null,wrapper);
//        }
        eduResearch.setEnable(!eduResearch.getEnable());
        baseMapper.updateById(eduResearch);
    }

    @Override
    @Transactional
    public void removeResearchById(Long id) {
        EduResearch research = baseMapper.selectById(id);
        research.setIsRemoveAfterReview(false);
        baseMapper.updateById(research);
    }

    @Override
    public AdminResearchDTO getResearchById(Long id) {
        EduResearch research = baseMapper.selectById(id);
        Assert.notNull(research,"Invalid research");
//        Assert.isTrue(research.getReview() != ReviewStatus.APPLIED, "Research is under review");
        List<BasicMemberVo> members = researchMemberService.getMembersByResearchId(id);
        AdminResearchDTO dto = new AdminResearchDTO();
        BeanUtils.copyProperties(research,dto, "htmlBrBase64");
        dto.setMembers(members);
        return dto;
    }
}
