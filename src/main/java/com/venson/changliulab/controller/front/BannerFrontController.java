package com.venson.changliulab.controller.front;

import com.venson.changliulab.entity.pojo.CrmBanner;
import com.venson.changliulab.service.admin.CrmBannerService;
import com.venson.changliulab.utils.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/educms/front/banner")
public class BannerFrontController {
    private final CrmBannerService crmBannerService;

    public BannerFrontController(CrmBannerService crmBannerService) {
        this.crmBannerService = crmBannerService;
    }

    @GetMapping()
    public Result<List<CrmBanner>> getFrontBanner(){
        List<CrmBanner> list = crmBannerService.getActiveBannerFront();
        return Result.success(list);

    }
}
