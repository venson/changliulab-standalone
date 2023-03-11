package com.venson.changliulabstandalone.service.impl;

import com.venson.changliulabstandalone.entity.pojo.AdminUser;
import com.venson.changliulabstandalone.entity.SecurityUser;
import com.venson.changliulabstandalone.entity.UserInfoBO;
import com.venson.changliulabstandalone.service.admin.AdminPermissionService;
import com.venson.changliulabstandalone.service.admin.AdminUserService;
import com.venson.changliulabstandalone.entity.enums.UserType;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 自定义userDetailsService - 认证用户详情
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
@Service("adminUserDetailsService")
public class AdminUserDetailsServiceImpl implements UserDetailsService {

    private final AdminUserService adminUserService;

    private final AdminPermissionService adminPermissionService;

    public AdminUserDetailsServiceImpl(AdminUserService adminUserService, AdminPermissionService adminPermissionService) {
        this.adminUserService = adminUserService;
        this.adminPermissionService = adminPermissionService;
    }

    /***
     * 根据账号获取用户信息
     * @param username:
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 从数据库中取出用户信息
        AdminUser adminUser = adminUserService.selectByUsername(username);

        // 判断用户是否存在
        if (null == adminUser){
            throw new UsernameNotFoundException("User not exists");
        }
        // 返回UserDetails实现类
        UserInfoBO userInfoBO = new UserInfoBO();
        BeanUtils.copyProperties(adminUser,userInfoBO);
        userInfoBO.setType(UserType.MEMBER);

        List<String> authorities = adminPermissionService.selectPermissionValueByUserId(adminUser.getId());
        SecurityUser securityUser= new SecurityUser(userInfoBO);
        securityUser.setPermissionValueList(authorities);
        return securityUser;
    }

}
