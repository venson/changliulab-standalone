package com.venson.changliulab.controller.front;

import com.venson.changliulab.entity.TokenVo;
import com.venson.changliulab.entity.vo.UserLogin;
import com.venson.changliulab.entity.vo.front.FrontUserDTO;
import com.venson.changliulab.service.LoginService;
import com.venson.changliulab.utils.Result;
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
