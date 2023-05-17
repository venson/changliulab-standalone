package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.SecurityUser;
import com.venson.changliulabstandalone.entity.TokenVo;
import com.venson.changliulabstandalone.entity.vo.UserLogin;
import com.venson.changliulabstandalone.exception.CustomizedException;
import com.venson.changliulabstandalone.service.TokenManager;
import com.venson.changliulabstandalone.utils.Assert;
import com.venson.changliulabstandalone.utils.ResUtils;
import com.venson.changliulabstandalone.utils.Result;
import com.venson.changliulabstandalone.config.security.PasswordNotFoundBCryptEncoded;
import com.venson.changliulabstandalone.entity.UserContextInfoBO;
import com.venson.changliulabstandalone.entity.UserInfoBO;
import com.venson.changliulabstandalone.constant.AuthConstants;
import com.venson.changliulabstandalone.utils.ContextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 */
@RestController()
@RequestMapping("/auth/admin/login")
public class AdminLoginController {

    @Autowired
    private UserDetailsService adminUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @PostMapping()
    public ResponseEntity<TokenVo> login(@RequestBody UserLogin user){
        SecurityUser securityUser = (SecurityUser) adminUserDetailsService.loadUserByUsername(user.getUsername());
        if(securityUser==null){
            passwordEncoder.matches("user not found", PasswordNotFoundBCryptEncoded.instance);
        }else{
            boolean matches = passwordEncoder.matches(user.getPassword(),securityUser.getPassword());
            if(matches){
                UserInfoBO tokenInfoBO =securityUser.getCurrentUserInfo();
                UserContextInfoBO userContextInfoBO = new UserContextInfoBO();
                BeanUtils.copyProperties(tokenInfoBO,userContextInfoBO);
                userContextInfoBO.setPermissionValueList(securityUser.getPermissionValueList());
                String redisKey = String.join(":", AuthConstants.ADMIN_PREFIX ,tokenInfoBO.getId().toString());
                String token = tokenManager.createToken(redisKey);
                userContextInfoBO.setToken(token);
                // store UserContextInfoBO to redis
                redisTemplate.opsForValue().set(redisKey, userContextInfoBO,
                        AuthConstants.EXPIRE_24H_S, TimeUnit.SECONDS);
                return ResUtils.ok(new TokenVo(token,securityUser.getPermissionValueList()));

            }
        }
        return ResUtils.unAuthorized();
    }
    @PostMapping("logout")
    public ResponseEntity<String> logout(){
        UserContextInfoBO userContext = ContextUtils.getUserContext();
        Assert.notNull(userContext,new CustomizedException("Not logged in"));
        tokenManager.removeToken(userContext.getToken());
        return ResUtils.ok();
    }
}
