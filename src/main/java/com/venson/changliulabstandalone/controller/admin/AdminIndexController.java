package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.dto.UserInfoDTO;
import com.venson.changliulabstandalone.entity.dto.UserPermissionsDTO;
import com.venson.changliulabstandalone.service.LoginService;
import com.venson.changliulabstandalone.utils.ResUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth/admin/index")
@RequiredArgsConstructor
public class AdminIndexController {

    private final LoginService loginService;

    /**
     * 根据token获取用户信息
     */
    @GetMapping("info")
    public ResponseEntity<UserInfoDTO> info(){
        Long id= loginService.getUserId();
        return ResUtils.ok(loginService.getAdminUserInfo(id));
        //获取当前登录用户用户名
    }

    /**
     * 获取菜单
     */
    @GetMapping("permissions")
    public ResponseEntity<UserPermissionsDTO> getPermission(){
        UserPermissionsDTO dto = loginService.getPermissionsById();
        return ResUtils.ok(dto);
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(){
        return ResUtils.ok();
    }

}
