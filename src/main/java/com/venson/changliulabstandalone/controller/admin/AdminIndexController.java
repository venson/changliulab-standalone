package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.dto.MenuDTO;
import com.venson.changliulabstandalone.entity.dto.UserInfoDTO;
import com.venson.changliulabstandalone.service.LoginService;
import com.venson.changliulabstandalone.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/admin/index")
public class AdminIndexController {

    @Autowired
    private LoginService loginService;

    /**
     * 根据token获取用户信息
     */
    @GetMapping("info")
    public Result<UserInfoDTO> info(){
        Long id= loginService.getUserId();
        return Result.success(loginService.getAdminUserInfo(id));
        //获取当前登录用户用户名
    }

    /**
     * 获取菜单
     */
    @GetMapping("menu")
    public Result<List<MenuDTO>> getMenu(){
        Long id= loginService.getUserId();

        return Result.success(loginService.getAdminMenu(id));
    }

    @PostMapping("logout")
    public Result<String> logout(){
        return Result.success();
    }

}
