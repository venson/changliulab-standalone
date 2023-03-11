package com.venson.changliulabstandalone.controller.front;

import com.venson.changliulabstandalone.entity.vo.FrontUserResetPasswordVo;
import com.venson.changliulabstandalone.entity.vo.RegistrationVo;
import com.venson.changliulabstandalone.service.front.FrontUserService;
import com.venson.changliulabstandalone.utils.Result;
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
