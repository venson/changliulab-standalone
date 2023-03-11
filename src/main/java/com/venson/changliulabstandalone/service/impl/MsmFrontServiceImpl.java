package com.venson.changliulabstandalone.service.impl;

import com.venson.changliulabstandalone.entity.enums.SendStatus;
import com.venson.changliulabstandalone.exception.CustomizedException;
import com.venson.changliulabstandalone.service.front.MsmFrontService;
import com.venson.changliulabstandalone.service.MsmService;
import com.venson.changliulabstandalone.utils.RandomString;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MsmFrontServiceImpl implements MsmFrontService {
    @Autowired
    private MsmService msmService;


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public SendStatus getSecurityCode(String emailUrl) {
        emailUrl = emailUrl.trim().replace("\"", "");
        if(!StringUtils.hasText(emailUrl)){
            return SendStatus.FAILED;
        }
        int count;
        String code;
        String emailUrlCount = emailUrl + ":count";
        log.info("changliulabstandalone Code to Email:" + emailUrl);
        String oldCode = redisTemplate.opsForValue().get(emailUrl);
        String countString = redisTemplate.opsForValue().get(emailUrlCount);
        if(StringUtils.hasText(countString)){
            count = Integer.parseInt(countString);
            if(count <3){
                redisTemplate.opsForValue().increment(emailUrlCount);
            }else{
                return SendStatus.TOO_MANY_ATTEMPTS;
            }
        }else{
            redisTemplate.opsForValue().set(emailUrlCount,"1",24,TimeUnit.HOURS);
        }
        if(StringUtils.hasText(oldCode)){
            code =  oldCode;
        } else{
            code = RandomString.randomCode();
        }
        try {
            msmService.sendCode(emailUrl, code,"changliulabstandalone Code",
                    "Registration changliulabstandalone Code",20);
            redisTemplate.opsForValue().set(emailUrl, code, 5, TimeUnit.MINUTES);
        } catch (MessagingException e) {
            throw new CustomizedException(30000, "Email sent failed");
        }
        return SendStatus.SUCCESS;
    }
}
