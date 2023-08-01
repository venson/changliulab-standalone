package com.venson.changliulab.service;

import com.venson.changliulab.entity.TokenVo;
import com.venson.changliulab.entity.dto.UserInfoDTO;
import com.venson.changliulab.entity.dto.UserPermissionsDTO;
import com.venson.changliulab.entity.vo.UserLogin;
import com.venson.changliulab.entity.vo.front.FrontUserDTO;

public interface LoginService {



    UserPermissionsDTO getPermissionsById();

    UserInfoDTO getAdminUserInfo(Long id);

    FrontUserDTO getFrontUserInfo();

    void frontLogout();

    TokenVo frontLogin(UserLogin userLogin);

    Long getUserId();
}
