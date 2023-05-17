package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.BannerVo;
import com.venson.changliulabstandalone.entity.pojo.CrmBanner;
import com.venson.changliulabstandalone.entity.dto.BannerDTO;
import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.service.admin.CrmBannerService;
import com.venson.changliulabstandalone.utils.PageResponse;
import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
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
@RequiredArgsConstructor
public class BannerAdminController {

    private final CrmBannerService service;


    @GetMapping("{page}/{limit}")
    @PreAuthorize("hasAuthority('Banner.READ')")
    public ResponseEntity<PageResponse<CrmBanner>> pageBanner(@PathVariable Integer page, @PathVariable Integer limit){
        PageResponse<CrmBanner> pageRes = service.getPageBanner(page, limit);
        return ResUtils.ok(pageRes);

    }
    @GetMapping()
    @PreAuthorize("hasAuthority('Banner.READ')")
    public ResponseEntity<PageResponse<CrmBanner>> getPageBanner(PageQueryVo vo){
        PageResponse<CrmBanner> pageRes = service.getPageBanner(vo.page(),vo.perPage());
        return ResUtils.ok(pageRes);

    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('Banner.READ')")
    public ResponseEntity<CrmBanner> getBanner(@PathVariable Long id){
        CrmBanner crmBanner = service.getById(id);
        return ResUtils.ok(crmBanner);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('Banner.CREATE')")
    @CacheEvict("enabledBannerList")
    public ResponseEntity<Long> addBanner(@RequestBody BannerDTO banner){
        Long id = service.addBanner(banner);
        return ResUtils.ok(id);
    }
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('Banner.EDIT')")
    public ResponseEntity<String> updateBanner(@PathVariable Long id,@Valid @RequestBody BannerVo crmBanner){
        service.updateBanner(id,crmBanner);
        return ResUtils.ok();
    }
    @PutMapping("/enable/{id}")
    @PreAuthorize("hasAuthority('Banner.EDIT')")
    @CacheEvict("enabledBannerList")
    public ResponseEntity<String> switchEnableBanner(@PathVariable Long id){
        service.switchEnableBanner(id);
        return ResUtils.ok();
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Banner.REMOVE')")
    @CacheEvict("enabledBannerList")
    public ResponseEntity<String> deleteBanner(@PathVariable Long id){
        service.removeById(id);
        return ResUtils.ok();
    }
//    @DeleteMapping("batch")
//    @PreAuthorize("hasAuthority('Banner.REMOVE')")
//    @CacheEvict("enabledBannerList")
//    public ResponseEntity<String> deleteBannerBatch(@RequestBody List<String> list){
//        service.removeBatchByIds(list);
//        return ResUtils.ok();
//    }
}
