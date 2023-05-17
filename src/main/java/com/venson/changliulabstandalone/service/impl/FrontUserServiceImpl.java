package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.FrontUser;
import com.venson.changliulabstandalone.entity.vo.FrontUserResetPasswordVo;
import com.venson.changliulabstandalone.entity.vo.RegistrationVo;
import com.venson.changliulabstandalone.entity.vo.ResetPasswordVo;
import com.venson.changliulabstandalone.entity.vo.front.FrontUserDTO;
import com.venson.changliulabstandalone.exception.CustomizedException;
import com.venson.changliulabstandalone.mapper.FrontUserMapper;
import com.venson.changliulabstandalone.service.admin.AdminUserService;
import com.venson.changliulabstandalone.service.front.FrontUserService;
import com.venson.changliulabstandalone.service.MsmService;
import com.venson.changliulabstandalone.utils.RandomString;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author venson
 * @since 2022-05-24
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FrontUserServiceImpl extends ServiceImpl<FrontUserMapper, FrontUser> implements FrontUserService {

    private final StringRedisTemplate stringRedisTemplate;

    private final PasswordEncoder passwordEncoder;


    private final MsmService msmService;

    private final AdminUserService adminUserService;




    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegistrationVo vo) {
        String changliulabstandaloneCode = vo.getSecurityCode().trim();
        String nickName = vo.getUsername().trim();
        String email = vo.getEmail().trim();
        String password = vo.getPassword();
        if(!StringUtils.hasText(email) || !StringUtils.hasText(nickName)
            || !StringUtils.hasText(password) || !StringUtils.hasText(changliulabstandaloneCode)){
            throw new CustomizedException(20001, "invalid information");
        }

        String code = stringRedisTemplate.opsForValue().get(email);
        if(!changliulabstandaloneCode.equals(code)){
            throw new CustomizedException(20001, "changliulabstandalone code error");
        }

        QueryWrapper<FrontUser> wrapper = new QueryWrapper<>();
        wrapper.eq("email",email);
        Long count = baseMapper.selectCount(wrapper);
        if (count >0){
            throw new CustomizedException(200001, "duplicate email address");
        }

        FrontUser frontUser = new FrontUser();
        frontUser.setUsername(vo.getUsername());
        frontUser.setEmail(vo.getEmail());
        log.info(frontUser.toString());
        frontUser.setPassword(passwordEncoder.encode(password));
        baseMapper.insert(frontUser);
        stringRedisTemplate.delete(email);


    }

//    @Override
//    public List<FrontUser> getMemberList(String filter) {
//        LambdaQueryWrapper<FrontUser> wrapper = new LambdaQueryWrapper<>();
//        wrapper.select(FrontUser::getId,
//                FrontUser::getUsername,
//                FrontUser::getEmail);
//        if(StringUtils.hasText(filter)){
//           wrapper.like(FrontUser::getUsername,filter).or()
//                   .like(FrontUser::getEmail,filter);
//        }
//        return baseMapper.selectList(wrapper);
//    }

    @Override
    public FrontUser selectByUsername(String username) {
        LambdaQueryWrapper<FrontUser> wrapper = new LambdaQueryWrapper<>();
        if(username.contains("@")){
            wrapper.eq(FrontUser::getEmail, username);
        }else{
            wrapper.eq(FrontUser::getUsername,username);
        }
        return baseMapper.selectOne(wrapper);
    }

    public FrontUser getUserByUsernameEmail(String username, String email) {
        LambdaQueryWrapper<FrontUser> wrapper= new LambdaQueryWrapper<>();
        wrapper.eq(FrontUser::getUsername,username).eq(FrontUser::getEmail,email);
        return baseMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(FrontUserResetPasswordVo vo) {
        String username = vo.getUsername().trim();
        String email = vo.getEmail().trim();
        FrontUser user = getUserByUsernameEmail(username,email);
        if(!StringUtils.hasText(username) ||
                !StringUtils.hasText(email) || !email.contains("@")){
            throw new CustomizedException(20001,"invalid username and email");
        }
        if(user == null){
            throw new CustomizedException(20001,"Username and email do not match");
        }
        String randomPassword = RandomString.randomCode(10);
        user.setPassword(passwordEncoder.encode(randomPassword));
        ResetPasswordVo passwordVo = new ResetPasswordVo();
        passwordVo.setEmail(user.getEmail());
        passwordVo.setRandomPassword(randomPassword);
        try {
            msmService.sendResetPasswordEmail(user.getEmail(),randomPassword);
        } catch (MessagingException e) {
            throw new CustomizedException(20001,"email send failed");
        }
    }

    @Override
    @Cacheable(value = FrontCacheConst.USER_NAME,key =FrontCacheConst.USER_FRONT_KEY +"+#userId")
    public FrontUserDTO getFrontUserById(Long userId) {
        LambdaQueryWrapper<FrontUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FrontUser::getId, userId)
                .select(FrontUser::getId, FrontUser::getEmail, FrontUser::getUsername);
        FrontUser frontUser = baseMapper.selectOne(wrapper);
        FrontUserDTO frontUserVo = new FrontUserDTO();
        BeanUtils.copyProperties(frontUser,frontUserVo);
        return frontUserVo;
    }

}
