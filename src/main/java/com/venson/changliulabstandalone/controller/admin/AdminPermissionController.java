package com.venson.changliulabstandalone.controller.admin;


import com.venson.changliulabstandalone.entity.pojo.AdminPermission;
import com.venson.changliulabstandalone.entity.dto.AdminPermissionDTO;
import com.venson.changliulabstandalone.service.admin.AdminPermissionService;
import com.venson.changliulabstandalone.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 权限 菜单管理
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/auth/admin/permission")
public class AdminPermissionController {

    @Autowired
    private AdminPermissionService adminPermissionService;


    @GetMapping
    public Result<List<AdminPermissionDTO>> getAllPermissionTree() {
        List<AdminPermissionDTO> list =  adminPermissionService.doGetAllPermissionTree();
        return Result.success(list);
    }

    @DeleteMapping("{id}")
    public Result<String> removePermissionById(@PathVariable Long id) {
        adminPermissionService.doRemovePermissionById(id);
        return Result.success();
    }


    @GetMapping("{roleId}")
    public Result<List<Long>> getPermissionsByRoleId(@PathVariable Long roleId) {
        List<Long> list = adminPermissionService.doGetPermissionsIdsByRoleId(roleId);
        List<Long> ignoreList = adminPermissionService.getIgnorePermissionIds();
        list.removeAll(ignoreList);
        return Result.success(list);
    }



    @PostMapping()
    public Result<String> addPermission(@RequestBody AdminPermissionDTO permission) {
        adminPermissionService.addPermission(permission);
        return Result.success();
    }

    @PutMapping("{id}")
    public Result<String> updatePermission(@PathVariable Long id,@RequestBody AdminPermission adminPermission) {
        adminPermissionService.doUpdatePermission(id,adminPermission);
//        adminPermissionService.updateById(adminPermission);
        return Result.success();
    }

}

