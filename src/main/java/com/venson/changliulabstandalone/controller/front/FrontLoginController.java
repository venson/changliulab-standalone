package com.venson.changliulabstandalone.controller.front;

import com.venson.changliulabstandalone.entity.TokenVo;
import com.venson.changliulabstandalone.entity.vo.UserLogin;
import com.venson.changliulabstandalone.entity.vo.front.FrontUserDTO;
import com.venson.changliulabstandalone.service.LoginService;
import com.venson.changliulabstandalone.utils.Result;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth/front")
public class FrontLoginController {
    @Autowired
    private LoginService loginService;


    @GetMapping("index/info")
    public Result<FrontUserDTO> getMemberInfo(){
        FrontUserDTO member = loginService.getFrontUserInfo();
        return Result.success(member);
    }
    @PostMapping("login")
    public Result<TokenVo> login(@Valid @RequestBody UserLogin userLogin){
        TokenVo token = loginService.frontLogin(userLogin);
        return Result.success(token);
    }

    @PostMapping("logout")
    public Result<String> logout(){
        loginService.frontLogout();
        return Result.success();
    }

}
