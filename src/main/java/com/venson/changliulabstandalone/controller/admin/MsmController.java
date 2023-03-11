package com.venson.changliulabstandalone.controller.admin;

import com.venson.changliulabstandalone.entity.UserContextInfoBO;
import com.venson.changliulabstandalone.entity.vo.ResetPasswordVo;
import com.venson.changliulabstandalone.exception.CustomizedException;
import com.venson.changliulabstandalone.service.MsmService;
import com.venson.changliulabstandalone.utils.ContextUtils;
import com.venson.changliulabstandalone.utils.RandomString;
import com.venson.changliulabstandalone.utils.Result;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/edumsm/admin/msm")
@Slf4j
public class MsmController {

    private final MsmService msmService;
    private final StringRedisTemplate redisTemplate;

    public MsmController(MsmService msmService ,StringRedisTemplate redisTemplate) {
        this.msmService = msmService;
        this.redisTemplate = redisTemplate;
    }

    @PostMapping(value = "securityCode")
    public Result<String> sendEmail(@RequestBody String emailUrl){
        log.info("changliulabstandalone Code to Email:" + emailUrl);
        Long expireTime = redisTemplate.getExpire(emailUrl, TimeUnit.MINUTES);
        if(expireTime!=null && expireTime>= 1){
            return Result.error("Please Wait");
        }
        String code = RandomString.randomCode();
        try {
            msmService.sendCode(emailUrl, code,"Security Code",
                    "Registration Security Code",20);
            redisTemplate.opsForValue().set(emailUrl, code, 20, TimeUnit.MINUTES);
        } catch (MessagingException e) {
            throw new CustomizedException(30000, "Email sent failed");
        }
        return Result.success();
    }
    @PostMapping(value = "resetPassword")
    public Result<String> resetEmail(@RequestBody ResetPasswordVo passwordVo){
        UserContextInfoBO userContext = ContextUtils.getUserContext();
        Assert.notNull(userContext,"Invalid user");
        Assert.isTrue(userContext.getEmail().equals(passwordVo.getEmail()),"Email not match");
        log.info("reset Password to Email:" + passwordVo.getEmail());
        try {
            msmService.sendCode(passwordVo.getEmail(),
                    passwordVo.getRandomPassword(),"New Password", "Reset Password",0);
        } catch (MessagingException e) {

            throw new CustomizedException(30000, "Email sent failed");
        }
        return Result.success();
    }
}
