package com.venson.changliulab.service.admin;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.BannerVo;
import com.venson.changliulab.entity.pojo.CrmBanner;
import com.venson.changliulab.entity.dto.BannerDTO;
import com.venson.changliulab.utils.PageResponse;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author venson
 * @since 2022-05-19
 */
public interface CrmBannerService extends IService<CrmBanner> {


    List<CrmBanner> getActiveBannerFront();

    PageResponse<CrmBanner> getPageBanner(Integer page, Integer limit);

    void switchEnableBanner(Long id);

    Long addBanner(BannerDTO banner);

    void updateBanner(Long id, BannerVo crmBanner);
}
