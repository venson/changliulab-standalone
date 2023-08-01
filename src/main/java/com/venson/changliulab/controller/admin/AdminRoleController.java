package com.venson.changliulab.controller.admin;


import com.venson.changliulab.entity.pojo.AdminRole;
import com.venson.changliulab.entity.dto.AdminRoleDTO;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.service.admin.AdminRoleService;
import com.venson.changliulab.utils.PageResponse;
import com.venson.changliulab.utils.PageUtil;
import com.venson.changliulab.utils.ResUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/auth/admin/role")
@RequiredArgsConstructor
public class AdminRoleController {

    private final AdminRoleService adminRoleService;

    @GetMapping()
    @PreAuthorize("hasAuthority('Role.READ')")
    public ResponseEntity<PageResponse<AdminRole>> getPage(PageQueryVo vo){
        PageResponse<AdminRole> pages =  adminRoleService.getPage(vo);
        return ResponseEntity.ok(pages);

    }

    @GetMapping("{page}/{limit}")
    @PreAuthorize("hasAuthority('Role.READ')")
    public ResponseEntity<PageResponse<AdminRole>> index(
            @PathVariable Long page,
            @PathVariable Long limit,
            AdminRole adminRole) {
        Page<AdminRole> pageParam = new Page<>(page, limit);
        QueryWrapper<AdminRole> wrapper = new QueryWrapper<>();
        if(!ObjectUtils.isEmpty(adminRole.getRoleName())) {
            wrapper.like("role_name", adminRole.getRoleName());
        }
        adminRoleService.page(pageParam,wrapper);
        PageResponse<AdminRole> pageRes = PageUtil.toBean(pageParam);
        return ResponseEntity.ok(pageRes);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('Role.READ')")
    public ResponseEntity<AdminRoleDTO> get(@PathVariable Long id) {
        AdminRoleDTO role = adminRoleService.getRoleById(id);
//        AdminRole adminRole = adminRoleService.getById(id);
        return ResponseEntity.ok(role);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('Role.CREATE')")
    public ResponseEntity<Long> addRole(@RequestBody AdminRoleDTO rolePermissionDTO) {
        Long id = adminRoleService.addRoleWithPermissions(rolePermissionDTO);
        return ResponseEntity.ok(id);
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('Role.EDIT')")
    public ResponseEntity<String> updateById(@RequestBody AdminRoleDTO rolePermissionDTO) {
        adminRoleService.updateRoleWithPermissions(rolePermissionDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('Role.REMOVE')")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        adminRoleService.removeRoleById(id);
        return ResponseEntity.ok().build();
    }

//    @DeleteMapping("batchRemove")
//    public ResponseEntity<String> batchRemove(@RequestBody List<String> idList) {
//        adminRoleService.removeByIds(idList);
//        return ResponseEntity.ok().build();
//    }
}

