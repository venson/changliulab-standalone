package com.venson.changliulab.service.front.impl;

import com.venson.changliulab.constant.FrontCacheConst;
import com.venson.changliulab.entity.pojo.EduMethodology;
import com.venson.changliulab.entity.UserContextInfoBO;
import com.venson.changliulab.entity.enums.UserType;
import com.venson.changliulab.entity.front.dto.MethodologyFrontDTO;
import com.venson.changliulab.service.admin.EduMethodologyService;
import com.venson.changliulab.service.front.MethodologyFrontService;
import com.venson.changliulab.utils.Assert;
import com.venson.changliulab.utils.ContextUtils;
import com.venson.changliulab.utils.PageResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class MethodologyFrontServiceImpl implements MethodologyFrontService {
    @Autowired
    private EduMethodologyService methodologyService;
    @Override
//    @Cacheable(value = FrontCacheConst.METHODOLOGY_PAGE_NAME, key="#current")
    public PageResponse<EduMethodology> getPageMethodology(Integer current, Integer size) {
        Assert.isTrue(current >0 && size>0,"Invalid Params");
        boolean isMember = false;
        try{
            UserContextInfoBO userContext = ContextUtils.getUserContext();
            if(userContext!=null){
                isMember =  UserType.MEMBER.equals(userContext.getType());
            }
        }catch (Exception ignored){}
        return methodologyService.getMethodologyFrontPage(current, size,isMember);
    }

    @Override
    @Cacheable(value = FrontCacheConst.METHODOLOGY_NAME, key="#id")
    public MethodologyFrontDTO getMethodologyById(Long id) {
        EduMethodology methodology = methodologyService.getById(id);
        MethodologyFrontDTO dto = new MethodologyFrontDTO();
        BeanUtils.copyProperties(methodology,dto);
        return dto;
    }
}
