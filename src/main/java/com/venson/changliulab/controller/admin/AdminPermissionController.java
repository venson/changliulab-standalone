package com.venson.changliulab.controller.admin;


import com.venson.changliulab.entity.dto.AdminPermissionTree;
import com.venson.changliulab.entity.dto.BasicListDTO;
import com.venson.changliulab.entity.pojo.AdminPermission;
import com.venson.changliulab.entity.dto.AdminPermissionDTO;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.service.admin.AdminPermissionService;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.ResUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequiredArgsConstructor
public class AdminPermissionController {

    private final AdminPermissionService adminPermissionService;


    @GetMapping("permissions")
    @PreAuthorize("hasAuthority('Permission.READ')")
    public ResponseEntity<BasicListDTO<AdminPermissionTree>> getAllPermissionTree() {
        List<AdminPermissionTree>  tree=  adminPermissionService.doGetAllPermission();
        return ResponseEntity.ok(BasicListDTO.of(tree));
    }
    @GetMapping
    @PreAuthorize("hasAuthority('Permission.READ')")
    public ResponseEntity<PageResponse<AdminPermission>> getPage(PageQueryVo vo) {
        PageResponse<AdminPermission>  page=  adminPermissionService.getPage(vo);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Permission.REMOVE')")
    public ResponseEntity<AdminPermission> removePermissionById(@PathVariable Long id) {
        AdminPermission adminPermission = adminPermissionService.doRemovePermissionById(id);
        return ResponseEntity.ok(adminPermission);
    }


    @GetMapping("{roleId}")
    @PreAuthorize("hasAuthority('Permission.READ')")
    public ResponseEntity<List<Long>> getPermissionsByRoleId(@PathVariable Long roleId) {
        List<Long> list = adminPermissionService.doGetPermissionsIdsByRoleId(roleId);
        return ResponseEntity.ok(list);
    }

    @GetMapping("alive")
    public ResponseEntity<String> checkAlive(){
        return ResponseEntity.ok().build();
    }



    @PostMapping()
    @PreAuthorize("hasAuthority('Permission.CREATE')")
    public ResponseEntity<Long> addPermission(@RequestBody AdminPermissionDTO permission) {
        Long id = adminPermissionService.addPermission(permission);
        return ResponseEntity.ok(id);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('Permission.EDIT')")
    public ResponseEntity<String> updatePermission(@PathVariable Long id,@RequestBody AdminPermission adminPermission) {
        adminPermissionService.doUpdatePermission(id,adminPermission);
//        adminPermissionService.updateById(adminPermission);
        return ResponseEntity.ok().build();
    }

}

