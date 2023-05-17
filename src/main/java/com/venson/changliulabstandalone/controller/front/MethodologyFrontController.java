package com.venson.changliulabstandalone.controller.front;

import com.venson.changliulabstandalone.entity.UserContextInfoBO;
import com.venson.changliulabstandalone.entity.enums.UserType;
import com.venson.changliulabstandalone.entity.front.dto.MethodologyFrontDTO;
import com.venson.changliulabstandalone.entity.pojo.EduMethodology;
import com.venson.changliulabstandalone.service.front.MethodologyFrontService;
import com.venson.changliulabstandalone.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("eduservice/front/methodology")
@Slf4j
public class MethodologyFrontController {

    @Autowired
    private MethodologyFrontService methodologyFrontService;
    @GetMapping("{current}/{size}")
    public Result<PageResponse<EduMethodology>> getPageMethodology(@PathVariable Integer current, @PathVariable Integer size){
        PageResponse<EduMethodology> page = methodologyFrontService.getPageMethodology(current, size);
        return Result.success(page);
    }

    @GetMapping("{id}")
    public Result<MethodologyFrontDTO> getMethodologyById(@PathVariable Long id){
        MethodologyFrontDTO methodology = methodologyFrontService.getMethodologyById(id);
        boolean isMember = false;
        try{
            UserContextInfoBO userContext = ContextUtils.getUserContext();
            if(userContext!=null){
                isMember =  UserType.MEMBER.equals(userContext.getType());
            }
        }catch (Exception ignored){}
        Assert.isTrue(methodology.getIsPublic() || isMember, ExcUtils.unAuthorized());
        return Result.success(methodology);

    }
}
