package com.venson.changliulab.controller.front;

import com.venson.changliulab.entity.vo.FrontUserResetPasswordVo;
import com.venson.changliulab.entity.vo.RegistrationVo;
import com.venson.changliulab.service.front.FrontUserService;
import com.venson.changliulab.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/front/user")
public class UserFrontController {
    @Autowired
    private FrontUserService frontUserService;


    @PostMapping("register")
    public Result<String> register(@RequestBody RegistrationVo vo){
        frontUserService.register(vo);
        return Result.success();
    }
    @PostMapping("resetPassword")
    public Result<String> resetPassword(@RequestBody FrontUserResetPasswordVo vo){
        frontUserService.resetPassword(vo);
        return Result.success();
    }
}
