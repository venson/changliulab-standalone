package com.venson.changliulab.controller.admin;

import com.venson.changliulab.entity.SecurityUser;
import com.venson.changliulab.entity.TokenVo;
import com.venson.changliulab.entity.vo.UserLogin;
import com.venson.changliulab.exception.CustomizedException;
import com.venson.changliulab.service.TokenManager;
import com.venson.changliulab.utils.Assert;
import com.venson.changliulab.utils.ResUtils;
import com.venson.changliulab.config.security.PasswordNotFoundBCryptEncoded;
import com.venson.changliulab.entity.UserContextInfoBO;
import com.venson.changliulab.entity.UserInfoBO;
import com.venson.changliulab.constant.AuthConstants;
import com.venson.changliulab.utils.ContextUtils;
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
                return ResponseEntity.ok(new TokenVo(token,securityUser.getPermissionValueList()));

            }
        }
        return ResUtils.unAuthorized();
    }
    @PostMapping("logout")
    public ResponseEntity<String> logout(){
        UserContextInfoBO userContext = ContextUtils.getUserContext();
        Assert.notNull(userContext,new CustomizedException("Not logged in"));
        tokenManager.removeToken(userContext.getToken());
        return ResponseEntity.ok().build();
    }
}
