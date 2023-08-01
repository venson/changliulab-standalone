package com.venson.changliulab.controller.admin;

import com.venson.changliulab.entity.dto.UserInfoDTO;
import com.venson.changliulab.entity.dto.UserPermissionsDTO;
import com.venson.changliulab.service.LoginService;
import com.venson.changliulab.utils.ResUtils;
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
        return ResponseEntity.ok(loginService.getAdminUserInfo(id));
        //获取当前登录用户用户名
    }

    /**
     * 获取菜单
     */
    @GetMapping("permissions")
    public ResponseEntity<UserPermissionsDTO> getPermission(){
        UserPermissionsDTO dto = loginService.getPermissionsById();
        return ResponseEntity.ok(dto);
    }

    @PostMapping("logout")
    public ResponseEntity<String> logout(){
        return ResponseEntity.ok().build();
    }

}
