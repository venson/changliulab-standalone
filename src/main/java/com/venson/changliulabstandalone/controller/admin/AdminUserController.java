package com.venson.changliulabstandalone.controller.admin;
import com.venson.changliulabstandalone.entity.UserContextInfoBO;
import com.venson.changliulabstandalone.entity.pojo.AdminUser;
import com.venson.changliulabstandalone.entity.dto.AclUserDTO;
import com.venson.changliulabstandalone.entity.vo.UserPersonalVO;
import com.venson.changliulabstandalone.entity.vo.admin.PageQueryVo;
import com.venson.changliulabstandalone.service.admin.AdminRoleService;
import com.venson.changliulabstandalone.service.admin.AdminUserService;
import com.venson.changliulabstandalone.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.venson.changliulabstandalone.valid.UpdateGroup;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@RestController
@RequestMapping("/auth/admin/user")
public class AdminUserController {

    private final AdminUserService adminUserService;

    private final AdminRoleService adminRoleService;




    public AdminUserController(AdminUserService adminUserService, AdminRoleService adminRoleService) {
        this.adminUserService = adminUserService;
        this.adminRoleService = adminRoleService;
    }

    @GetMapping("{page}/{limit}")
    @PreAuthorize("hasAuthority('User.READ')")
    public ResponseEntity<PageResponse<AdminUser>> index(
            @PathVariable Long page,

            @PathVariable Long limit,

             AdminUser userQueryVo) {
        Page<AdminUser> pageParam = new Page<>(page, limit);
        QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
        if(!ObjectUtils.isEmpty(userQueryVo.getUsername())) {
            wrapper.like("username",userQueryVo.getUsername());
        }

        adminUserService.page(pageParam, wrapper);
        PageResponse<AdminUser> pageResponse = PageUtil.toBean(pageParam);
        return ResUtils.ok(pageResponse);
    }
    @GetMapping
    @PreAuthorize("hasAuthority('User.READ')")
    public ResponseEntity<PageResponse<AdminUser>> getPage(PageQueryVo vo){
        PageResponse<AdminUser> page = adminUserService.getPage(vo);
        return ResUtils.ok(page);
    }
    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('User.READ')")
    public ResponseEntity<AclUserDTO> get(@PathVariable Long id) {
        AclUserDTO user = adminUserService.getUserById(id);
        return ResUtils.ok(user);
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('User.CREATE')")
    public ResponseEntity<String> save(@RequestBody AclUserDTO user) {
        adminUserService.addUser(user);
        return ResUtils.ok();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAuthority('User.EDIT')")
    public ResponseEntity<String> updateById(@Validated(UpdateGroup.class) @RequestBody AclUserDTO user) {
        adminUserService.updateAclUser(user);
        return ResUtils.ok();
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('User.REMOVE')")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        adminUserService.removeUserById(id);
        return ResUtils.ok();
    }



    @GetMapping("personal/{id}")
    @PreAuthorize("hasAuthority('User.EDIT')")
    public ResponseEntity<String> resetRandomPasswordById(@PathVariable Long id){
        adminUserService.resetRandomPasswordById(id);
        return ResUtils.ok();
    }

    @PutMapping("personal")
    public ResponseEntity<String> updateUserPersonalInfo(@RequestBody UserPersonalVO UserPersonalVO){
        UserContextInfoBO userContext = ContextUtils.getUserContext();
        Assert.notNull(userContext, ExcUtils.unAuthorized());
        adminUserService.updateUserPersonalInfo(UserPersonalVO,userContext.getId());
        return ResUtils.ok();
    }
}

