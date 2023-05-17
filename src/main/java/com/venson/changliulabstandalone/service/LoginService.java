package com.venson.changliulabstandalone.service;

import com.venson.changliulabstandalone.entity.TokenVo;
import com.venson.changliulabstandalone.entity.dto.PermissionDTO;
import com.venson.changliulabstandalone.entity.dto.UserInfoDTO;
import com.venson.changliulabstandalone.entity.dto.UserPermissionsDTO;
import com.venson.changliulabstandalone.entity.vo.UserLogin;
import com.venson.changliulabstandalone.entity.vo.front.FrontUserDTO;

import java.util.List;

public interface LoginService {



    UserPermissionsDTO getPermissionsById();

    UserInfoDTO getAdminUserInfo(Long id);

    FrontUserDTO getFrontUserInfo();

    void frontLogout();

    TokenVo frontLogin(UserLogin userLogin);

    Long getUserId();
}
