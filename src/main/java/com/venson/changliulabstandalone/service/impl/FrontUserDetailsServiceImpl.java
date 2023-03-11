package com.venson.changliulabstandalone.service.impl;

import com.venson.changliulabstandalone.entity.FrontUser;
import com.venson.changliulabstandalone.entity.SecurityUser;
import com.venson.changliulabstandalone.service.front.FrontPermissionService;
import com.venson.changliulabstandalone.service.front.FrontUserService;
import com.venson.changliulabstandalone.entity.enums.UserType;
import com.venson.changliulabstandalone.entity.UserInfoBO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("frontUserDetailsService")
public class FrontUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private FrontUserService frontUserService;
    @Autowired
    private FrontPermissionService frontPermissionService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        FrontUser frontUser = frontUserService.selectByUsername(username);
        if(null == frontUser){
            throw new UsernameNotFoundException("User not exists");
        }
        UserInfoBO userInfoBO = new UserInfoBO();
        BeanUtils.copyProperties(frontUser,userInfoBO);
        // set User Type to normal user
        userInfoBO.setType(UserType.USER);

        List<String> authorities = frontPermissionService.selectPermissionValueByUserId(frontUser.getId());
        SecurityUser securityUser = new SecurityUser(userInfoBO);
        securityUser.setPermissionValueList(authorities);
        return securityUser;
    }
}
