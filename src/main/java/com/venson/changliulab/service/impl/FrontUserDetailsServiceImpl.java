package com.venson.changliulab.service.impl;

import com.venson.changliulab.entity.FrontUser;
import com.venson.changliulab.entity.SecurityUser;
import com.venson.changliulab.service.front.FrontPermissionService;
import com.venson.changliulab.service.front.FrontUserService;
import com.venson.changliulab.entity.enums.UserType;
import com.venson.changliulab.entity.UserInfoBO;
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
