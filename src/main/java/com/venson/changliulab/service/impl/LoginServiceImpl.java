package com.venson.changliulab.service.impl;

import com.venson.changliulab.config.security.PasswordNotFoundBCryptEncoded;
import com.venson.changliulab.constant.AdminCacheConst;
import com.venson.changliulab.entity.*;
import com.venson.changliulab.entity.dto.UserInfoDTO;
import com.venson.changliulab.entity.dto.UserPermissionsDTO;
import com.venson.changliulab.entity.enums.UserType;
import com.venson.changliulab.entity.pojo.*;
import com.venson.changliulab.entity.vo.UserLogin;
import com.venson.changliulab.entity.vo.front.FrontUserDTO;
import com.venson.changliulab.service.LoginService;
import com.venson.changliulab.service.TokenManager;
import com.venson.changliulab.service.admin.AdminPermissionService;
import com.venson.changliulab.service.admin.AdminRoleService;
import com.venson.changliulab.service.admin.AdminUserService;
import com.venson.changliulab.service.front.FrontUserService;
import com.venson.changliulab.utils.Assert;
import com.venson.changliulab.utils.ContextUtils;
import com.venson.changliulab.utils.ExcUtils;
import com.venson.changliulab.constant.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private TokenManager tokenManager;
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private UserDetailsService frontUserDetailsService;

    @Autowired
    private UserDetailsService adminUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final AdminUserService adminUserService;

    private final AdminRoleService adminRoleService;

    private final AdminPermissionService adminPermissionService;

    private final FrontUserService frontUserService;


    public LoginServiceImpl(AdminUserService adminUserService, AdminRoleService adminRoleService,
                            AdminPermissionService adminPermissionService,
                            FrontUserService frontUserService
    ) {
        this.adminUserService = adminUserService;
        this.adminRoleService = adminRoleService;
        this.adminPermissionService = adminPermissionService;
        this.frontUserService = frontUserService;
    }



    @Override
    @Cacheable(value = AdminCacheConst.USER_MENU_NAME, key = "#result.id")
    public UserPermissionsDTO getPermissionsById() {

        UserContextInfoBO userContext = ContextUtils.getUserContext();
        Assert.notNull(userContext,new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        List<String> permissionValueList = userContext.getPermissionValueList();
        UserPermissionsDTO dto = new UserPermissionsDTO();
        dto.setId(userContext.getId());
        dto.setPermissions(permissionValueList);
        return dto;
    }

    @Override
    @Cacheable(value = AdminCacheConst.USER_INFO_NAME, key="#id")
    public UserInfoDTO getAdminUserInfo(Long id) {

        AdminUser user = adminUserService.getById(id);
        Assert.notNull(user,new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        //根据用户id获取角色
        List<AdminRole> adminRoleList = adminRoleService.selectRoleByUserId(id);
        List<String> roleNameList = adminRoleList.stream().map(AdminRole::getRoleName).collect(Collectors.toList());
        if(roleNameList.size() == 0) {
            //前端框架必须返回一个角色，否则报错，如果没有角色，返回一个空角色
            roleNameList.add("");
        }

        //根据用户id获取操作权限值
        List<String> permissionValueList = adminPermissionService.selectPermissionValueByUserId(id);
//        redisTemplate.opsForValue().set(user.getUsername(), permissionValueList);

//        List<JSONObject> permissionList = getMenu(id);
//        List<JSONObject> permissionList = null;

        return new UserInfoDTO(user.getId(),user.getUsername(),user.getAvatar(),roleNameList,permissionValueList, null);
    }

    @Override
    public FrontUserDTO getFrontUserInfo() {
        UserContextInfoBO userContext = ContextUtils.getUserContext();
        Assert.notNull(userContext, ExcUtils.tokenExpired());
        if(UserType.MEMBER.equals(userContext.getType())){
            return adminUserService.getAdminUserForFrontById(userContext.getId());

        }else{
            return frontUserService.getFrontUserById(userContext.getId());

        }
    }

    @Override
    public void frontLogout() {
        UserContextInfoBO userContext = ContextUtils.getUserContext();
        Assert.notNull(userContext, ExcUtils.tokenExpired());
            tokenManager.removeToken(userContext.getToken());
    }

    @Override
    public TokenVo frontLogin(UserLogin userLogin) {

        String username= userLogin.getUsername().trim().replace("\"","");
        String password = userLogin.getPassword();
//        Assert.hasText(password, "Invalid email or password");
//        Assert.hasText(username, "Invalid email or password");
        SecurityUser securityUser;
        securityUser = (SecurityUser)frontUserDetailsService.loadUserByUsername(username);
        if(securityUser == null) {
            securityUser = (SecurityUser)adminUserDetailsService.loadUserByUsername(username);
        }
        if(securityUser ==null){
            passwordEncoder.matches("password not found", PasswordNotFoundBCryptEncoded.instance);

        }
        else{
            if(passwordEncoder.matches(password, securityUser.getPassword())){

                UserInfoBO tokenInfoBO =securityUser.getCurrentUserInfo();
                UserContextInfoBO userContextInfoBO = new UserContextInfoBO();
                BeanUtils.copyProperties(tokenInfoBO,userContextInfoBO);
                userContextInfoBO.setPermissionValueList(securityUser.getPermissionValueList());
                String redisKey = String.join(":", AuthConstants.USER_PREFIX ,tokenInfoBO.getId().toString());
                String token = tokenManager.createToken(redisKey);
                userContextInfoBO.setToken(token);
                // store UserContextInfoBO to redis
                redisTemplate.opsForValue().set(redisKey, userContextInfoBO,
                        AuthConstants.EXPIRE_24H_S, TimeUnit.SECONDS);
                return new TokenVo(token,securityUser.getPermissionValueList());
            }
        }
        return null;
    }

    @Override
    public Long getUserId() {
        UserContextInfoBO userContext = ContextUtils.getUserContext();
        Assert.notNull(userContext, ExcUtils.tokenExpired());
        Assert.isTrue(userContext.getType() == UserType.MEMBER && !userContext.getUsername().equals("anonymousUser"), ExcUtils.unAuthorized());
        return userContext.getId();
    }


}
