package com.venson.changliulabstandalone.service.front.impl;

import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.pojo.EduMethodology;
import com.venson.changliulabstandalone.entity.UserContextInfoBO;
import com.venson.changliulabstandalone.entity.enums.UserType;
import com.venson.changliulabstandalone.entity.front.dto.MethodologyFrontDTO;
import com.venson.changliulabstandalone.service.admin.EduMethodologyService;
import com.venson.changliulabstandalone.service.front.MethodologyFrontService;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.ContextUtils;
import com.venson.changliulabstandalone.utils.PageResponse;
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
