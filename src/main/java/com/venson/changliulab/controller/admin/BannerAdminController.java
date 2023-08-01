package com.venson.changliulab.controller.admin;

import com.venson.changliulab.entity.BannerVo;
import com.venson.changliulab.entity.pojo.CrmBanner;
import com.venson.changliulab.entity.dto.BannerDTO;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.service.admin.CrmBannerService;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.ResUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(pageRes);

    }
    @GetMapping()
    @PreAuthorize("hasAuthority('Banner.READ')")
    public ResponseEntity<PageResponse<CrmBanner>> getPageBanner(PageQueryVo vo){
        PageResponse<CrmBanner> pageRes = service.getPageBanner(vo.page(),vo.perPage());
        return ResponseEntity.ok(pageRes);

    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('Banner.READ')")
    public ResponseEntity<CrmBanner> getBanner(@PathVariable Long id){
        CrmBanner crmBanner = service.getById(id);
        return ResponseEntity.ok(crmBanner);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('Banner.CREATE')")
    @CacheEvict("enabledBannerList")
    public ResponseEntity<Long> addBanner(@RequestBody BannerDTO banner){
        Long id = service.addBanner(banner);
        return ResponseEntity.ok(id);
    }
    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('Banner.EDIT')")
    public ResponseEntity<String> updateBanner(@PathVariable Long id,@Valid @RequestBody BannerVo crmBanner){
        service.updateBanner(id,crmBanner);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/enable/{id}")
    @PreAuthorize("hasAuthority('Banner.EDIT')")
    @CacheEvict("enabledBannerList")
    public ResponseEntity<String> switchEnableBanner(@PathVariable Long id){
        service.switchEnableBanner(id);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Banner.REMOVE')")
    @CacheEvict("enabledBannerList")
    public ResponseEntity<String> deleteBanner(@PathVariable Long id){
        service.removeById(id);
        return ResponseEntity.ok().build();
    }
//    @DeleteMapping("batch")
//    @PreAuthorize("hasAuthority('Banner.REMOVE')")
//    @CacheEvict("enabledBannerList")
//    public ResponseEntity<String> deleteBannerBatch(@RequestBody List<String> list){
//        service.removeBatchByIds(list);
//        return ResponseEntity.ok().build();
//    }
}
