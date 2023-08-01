package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.BannerVo;
import com.venson.changliulab.entity.pojo.CrmBanner;
import com.venson.changliulab.entity.dto.BannerDTO;
import com.venson.changliulab.mapper.CrmBannerMapper;
import com.venson.changliulab.service.admin.CrmBannerService;
import com.venson.changliulab.utils.Assert;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-05-19
 */
@Service
public class CrmBannerServiceImp extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    @Override
    @Cacheable(value = FrontCacheConst.BANNER_NAME)
    public List<CrmBanner> getActiveBannerFront() {
        LambdaQueryWrapper<CrmBanner> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CrmBanner::getEnable,true)
                .select(CrmBanner::getId,CrmBanner::getTitle,CrmBanner::getImageUrl,CrmBanner::getImageOutlinkUrl);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public PageResponse<CrmBanner> getPageBanner(Integer page, Integer limit) {
        Page<CrmBanner> bannerPage = new Page<>(page, limit);
        LambdaQueryWrapper<CrmBanner> wrapper = Wrappers.lambdaQuery(CrmBanner.class).select(CrmBanner::getId, CrmBanner::getTitle, CrmBanner::getImageUrl
                , CrmBanner::getImageOutlinkUrl, CrmBanner::getEnable);
        baseMapper.selectPage(bannerPage,wrapper);
        return PageUtil.toBean(bannerPage);
    }

    @Override
    @CacheEvict(value = FrontCacheConst.BANNER_NAME, allEntries = true)
    public void switchEnableBanner(Long id) {
        CrmBanner banner = baseMapper.selectById(id);
        Assert.notNull(banner,"Banner not exits" );
        banner.setEnable(!banner.getEnable());
        baseMapper.updateById(banner);

    }

    @Override
    public Long addBanner(BannerDTO banner) {
        checkTitleUsable(null, banner.getTitle());
        CrmBanner crmBanner = new CrmBanner();
        BeanUtils.copyProperties(banner,crmBanner);
        baseMapper.insert(crmBanner);
        return crmBanner.getId();
    }

    @Override
    @CacheEvict(value = FrontCacheConst.BANNER_NAME,allEntries = true)
    public void updateBanner(Long id,  BannerVo banner) {
        Assert.isTrue(id.equals(banner.getId()), "Not valid modification");
        checkTitleUsable(id, banner.getTitle());
        LambdaUpdateWrapper<CrmBanner> wrapper = Wrappers.lambdaUpdate(CrmBanner.class)
                .eq(CrmBanner::getId,id)
                .set(CrmBanner::getTitle,banner.getTitle())
                .set(CrmBanner::getImageUrl,banner.getImageUrl())
                .set(CrmBanner::getImageOutlinkUrl,banner.getImageOutlinkUrl());
        baseMapper.update(null,wrapper);
    }

    private void checkTitleUsable(Long id, String title){
        LambdaQueryWrapper<CrmBanner> wrapper = Wrappers.lambdaQuery(CrmBanner.class).eq(CrmBanner::getTitle, title);
            wrapper.ne(id!=null,CrmBanner::getId, id);
        Assert.isTrue(baseMapper.exists(wrapper) , "Duplicated Title");
    }
}
