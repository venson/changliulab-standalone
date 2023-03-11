package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.BannerVo;
import com.venson.changliulabstandalone.entity.pojo.CrmBanner;
import com.venson.changliulabstandalone.entity.dto.BannerDTO;
import com.venson.changliulabstandalone.service.admin.CrmBannerService;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.Result;
import jakarta.validation.Valid;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 前端控制器
 * </p>
 *
 * @author venson
 * @since 2022-05-19
 */
@RestController
@RequestMapping("/educms/admin/banner")
public class BannerAdminController {

    private final CrmBannerService service;

    public BannerAdminController(CrmBannerService service) {
        this.service= service;
    }

    @GetMapping("{page}/{limit}")
    @PreAuthorize("hasAuthority('banner.list')")
    public Result<PageResponse<CrmBanner>> pageBanner(@PathVariable Integer page, @PathVariable Integer limit){
        PageResponse<CrmBanner> pageRes = service.getPageBanner(page, limit);
        return Result.success(pageRes);

    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('banner.list')")
    public Result<CrmBanner> getBanner(@PathVariable Long id){
        CrmBanner crmBanner = service.getById(id);
        return Result.success(crmBanner);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('banner.add')")
    @CacheEvict("enabledBannerList")
    public Result<Long> addBanner(@RequestBody BannerDTO banner){
        Long id = service.addBanner(banner);
        return Result.success(id);
    }
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('banner.edit')")
    @CacheEvict("enabledBannerList")
    public Result<String> updateBanner(@PathVariable Long id,@Valid @RequestBody BannerVo crmBanner){
        service.updateBanner(id,crmBanner);
        return Result.success();
    }
    @PutMapping("/switch/{id}")
    @PreAuthorize("hasAuthority('banner.edit')")
    @CacheEvict("enabledBannerList")
    public Result<String> switchEnableBanner(@PathVariable Long id){
        service.switchEnableBanner(id);
        return Result.success();
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('banner.remove')")
    @CacheEvict("enabledBannerList")
    public Result<String> deleteBanner(@PathVariable Long id){
        service.removeById(id);
        return Result.success();
    }
    @DeleteMapping("batch")
    @PreAuthorize("hasAuthority('banner.remove')")
    @CacheEvict("enabledBannerList")
    public Result<String> deleteBannerBatch(@RequestBody List<String> list){
        service.removeBatchByIds(list);
        return Result.success();
    }
}
