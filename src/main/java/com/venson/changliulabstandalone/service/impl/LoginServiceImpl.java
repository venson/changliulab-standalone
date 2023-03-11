package com.venson.changliulabstandalone.service.impl;

import com.venson.changliulabstandalone.config.security.PasswordNotFoundBCryptEncoded;
import com.venson.changliulabstandalone.constant.AdminCacheConst;
import com.venson.changliulabstandalone.entity.*;
import com.venson.changliulabstandalone.entity.dto.MenuDTO;
import com.venson.changliulabstandalone.entity.dto.UserInfoDTO;
import com.venson.changliulabstandalone.entity.enums.UserType;
import com.venson.changliulabstandalone.entity.pojo.*;
import com.venson.changliulabstandalone.entity.vo.UserLogin;
import com.venson.changliulabstandalone.entity.vo.front.FrontUserDTO;
import com.venson.changliulabstandalone.service.LoginService;
import com.venson.changliulabstandalone.service.TokenManager;
import com.venson.changliulabstandalone.service.admin.AdminPermissionService;
import com.venson.changliulabstandalone.service.admin.AdminRoleService;
import com.venson.changliulabstandalone.service.admin.AdminUserService;
import com.venson.changliulabstandalone.service.front.FrontUserService;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.ContextUtils;
import com.venson.changliulabstandalone.utils.ExcUtils;
import com.venson.changliulabstandalone.constant.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
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

//    /**
//     * 根据用户名获取用户登录信息
//     *
//     */
//    public UserInfoDTO getUserInfo(Long id) {
//        AdminUser user = adminUserService.getById(id);
//        Assert.notNull(user, "Invalid User");
//        //根据用户id获取角色
//        List<AdminRole> adminRoleList = adminRoleService.selectRoleByUserId(id);
//        List<String> roleNameList = adminRoleList.stream().map(AdminRole::getRoleName).collect(Collectors.toList());
//        if(roleNameList.size() == 0) {
//            //前端框架必须返回一个角色，否则报错，如果没有角色，返回一个空角色
//            roleNameList.add("");
//        }
//
//        //根据用户id获取操作权限值
//        List<String> permissionValueList = adminPermissionService.selectPermissionValueByUserId(id);
//        redisTemplate.opsForValue().set(user.getUsername(), permissionValueList);
//
////        List<JSONObject> permissionList = getMenu(id);
////        List<JSONObject> permissionList = null;
//
//        return new UserInfoDTO(user.getId(),user.getUsername(),user.getAvatar(),roleNameList,permissionValueList, null);
//
//    }

    /**
     * 根据用户名获取动态菜单
     */
//    public List<JSONObject> getMenu(String username) {
//        AdminUser user = adminUserService.selectByUsername(username);
//
//        //根据用户id获取用户菜单权限
//        return adminPermissionService.selectPermissionByUserId(user.getId());
//    }


    @Override
    @Cacheable(value = AdminCacheConst.USER_MENU_NAME, key = "#id")
    public List<MenuDTO> getAdminMenu(Long id) {
        List<AdminPermission> allMenus = adminPermissionService.getAllPermissions(false);
        List<Long> permissionIds = adminPermissionService.getMenuPermissionIdsByUserId(id);


        HashMap<Long, MenuDTO> menuMap = new HashMap<>();
        Map<Long, AdminPermission> allMenusMap = allMenus.stream().collect(Collectors.toMap(AdminPermission::getId, Function.identity()));
//        allPermissions.forEach(o->allPermissionsMap.put(o.getId(),o));
        permissionIds.forEach(o->createMenu(allMenusMap,menuMap,o));
        return new ArrayList<>(menuMap.values());
//        return adminPermissionService.doGetMenus(id);
    }

    @Override
    @Cacheable(value = AdminCacheConst.USER_INFO_NAME, key="#id")
    public UserInfoDTO getAdminUserInfo(Long id) {

        AdminUser user = adminUserService.getById(id);
        Assert.notNull(user, "Invalid User");
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
                return new TokenVo(token);
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


    private void createMenu(Map<Long, AdminPermission> map,Map<Long, MenuDTO> resultMap, Long id){
        AdminPermission permission = map.get(id);
        if(permission ==null){
            return;
        }
        if(permission.getPid()==1 && resultMap.containsKey(id)) {
            return;
        }
        MenuDTO menu = new MenuDTO();
        menu.setPath(permission.getPath());
        menu.setName("name_"+ permission.getId());
        menu.setComponent(permission.getComponent());
        menu.setMeta(new MenuMeta(permission.getName(), permission.getType()==2, permission.getIcon()));
        if(permission.getPid()==1){
            resultMap.put(permission.getId(),menu);
        } else if (permission.getType() == 1) {
            createMenu(map,resultMap, permission.getPid());
            resultMap.get(permission.getPid()).getChildren().add(menu);
        }else if(permission.getType()==2){
//            AdminPermission parentPermission = map.get(permission.getPid());
            Long grandParent = map.get(permission.getPid()).getPid();
            createMenu(map,resultMap,grandParent);

            resultMap.get(grandParent).getChildren().add(menu);
        }
    }
}
