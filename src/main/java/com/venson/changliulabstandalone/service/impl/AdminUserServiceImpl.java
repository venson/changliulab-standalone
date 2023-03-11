package com.venson.changliulabstandalone.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.venson.changliulabstandalone.constant.AdminCacheConst;
import com.venson.changliulabstandalone.constant.FrontCacheConst;
import com.venson.changliulabstandalone.entity.pojo.AdminUser;
import com.venson.changliulabstandalone.entity.pojo.AdminUserRole;
import com.venson.changliulabstandalone.entity.dto.AclUserDTO;
import com.venson.changliulabstandalone.entity.vo.ResetPasswordVo;
import com.venson.changliulabstandalone.entity.vo.UserPersonalVO;
import com.venson.changliulabstandalone.entity.vo.front.FrontUserDTO;
import com.venson.changliulabstandalone.exception.CustomizedException;
import com.venson.changliulabstandalone.mapper.AdminUserMapper;
import com.venson.changliulabstandalone.service.admin.AdminUserRoleService;
import com.venson.changliulabstandalone.service.admin.AdminUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.venson.changliulabstandalone.service.MsmService;
import com.venson.changliulabstandalone.utils.RandomString;
import com.venson.changliulabstandalone.entity.UserContextInfoBO;
import com.venson.changliulabstandalone.utils.ContextUtils;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
@Slf4j
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser> implements AdminUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminUserRoleService userRoleService;


    @Autowired
    private MsmService msmService;



    @Override
    public AdminUser selectByUsername(String username) {
        LambdaQueryWrapper<AdminUser> wrapper= Wrappers.lambdaQuery();
        boolean emailCheck = username.contains("@");
        wrapper.eq(!emailCheck,AdminUser::getUsername, username)
                .eq(emailCheck,AdminUser::getEmail,username);
        return baseMapper.selectOne(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void resetRandomPasswordById(Long id) {
        UserContextInfoBO userContext = ContextUtils.getUserContext();
        Assert.notNull(userContext,"Invalid user");
        String randomPassword = RandomString.randomCode(10);
        AdminUser user = baseMapper.selectById(id);
        Assert.isTrue(userContext.getEmail().equals(user.getEmail()),"Email not match");
        user.setRandomPassword(true);
        user.setPassword(passwordEncoder.encode(randomPassword));
        baseMapper.updateById(user);
        ResetPasswordVo passwordVo = new ResetPasswordVo();
        passwordVo.setEmail(user.getEmail());
        passwordVo.setRandomPassword(randomPassword);
        log.info("reset Password to Email:" + passwordVo.getEmail());
        try {
            msmService.sendCode(passwordVo.getEmail(),
                    passwordVo.getRandomPassword(),"New Password", "Reset Password",0);
        } catch (MessagingException e) {
            throw new CustomizedException(30000, "Email sent failed");
        }
    }

    @Override
    @CacheEvict(value = {AdminCacheConst.USER_INFO_NAME, AdminCacheConst.USER_MENU_NAME}, key ="#user.id" )
    public void updateAclUser(AclUserDTO user) {
        AdminUser adminUser = baseMapper.selectById(user.getId());
        BeanUtils.copyProperties(user, adminUser);
        baseMapper.updateById(adminUser);
        userRoleService.updateUserRole(user.getId(), user.getRoleIds());
    }

    @Override
    @CacheEvict(value = FrontCacheConst.USE_NAME,key = FrontCacheConst.USE_ADMIN_KEY + "+#userId")
    public void updateUserPersonalInfo(UserPersonalVO userPersonalVO, Long userId) {
        AdminUser adminUser = baseMapper.selectById(userId);
        boolean match = passwordEncoder.matches(userPersonalVO.getOld(), adminUser.getPassword());
        Assert.isTrue(match, "Password error");
        String newPassword = userPersonalVO.getAltered();
        if (newPassword != null) {
            adminUser.setPassword(passwordEncoder.encode(userPersonalVO.getAltered()));
            log.info(adminUser.getId() + "request password");
        }
        if (StringUtils.hasText(userPersonalVO.getNickname())) {
            adminUser.setNickName(userPersonalVO.getNickname());
        }
        if (userPersonalVO.getEmail() != null) {
            adminUser.setEmail(userPersonalVO.getEmail());
        }

        baseMapper.updateById(adminUser);
    }


    @Override
    public AclUserDTO getUserById(Long id) {
        AdminUser adminUser = baseMapper.selectById(id);
        LambdaQueryWrapper<AdminUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUserRole::getUserId, id).select(AdminUserRole::getRoleId);
        List<Long> roleIds = userRoleService.listObjs(wrapper, (o) -> Long.valueOf(o.toString()));
        AclUserDTO userDTO = new AclUserDTO();
        BeanUtils.copyProperties(adminUser, userDTO, "password");
        userDTO.setRoleIds(roleIds);
        return userDTO;

    }

    @Override
    @Transactional
    public void addUser(AclUserDTO user) {
        String email = user.getEmail();
        String username = user.getUsername();
        LambdaQueryWrapper<AdminUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUser::getNickName, username);
        if (baseMapper.selectCount(wrapper) != 0) {
            throw new CustomizedException(20001, "User name already exists");
        }
        wrapper.clear();
        wrapper.eq(AdminUser::getEmail, email);
        if (baseMapper.selectCount(wrapper) != 0) {
            throw new CustomizedException(20001, "Email already exists");
        }

        // add new User
        // 1. generate random password
        String password = RandomString.randomCode(10);
        AdminUser adminUser = new AdminUser();
        BeanUtils.copyProperties(user, adminUser);
        adminUser.setRandomPassword(true);
        adminUser.setPassword(passwordEncoder.encode(password));
        baseMapper.insert(adminUser);
        // 2. send password to email
        try {
            msmService.sendResetPasswordEmail(email,password);
        } catch (MessagingException e) {
            throw new CustomizedException(30000, "Can not send Email");
        }
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = {AdminCacheConst.USER_MENU_NAME, AdminCacheConst.USER_MENU_NAME}, key="#id"),
            @CacheEvict(value = FrontCacheConst.USE_NAME,key = FrontCacheConst.USE_ADMIN_KEY + "+#id")

    })
    public void removeUserById(Long id) {
        baseMapper.deleteById(id);
        LambdaQueryWrapper<AdminUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUserRole::getUserId, id);
        userRoleService.remove(wrapper);

    }

    @Override
    @Cacheable(value = FrontCacheConst.USE_NAME,key = FrontCacheConst.USE_ADMIN_KEY+ "+#id")
    public FrontUserDTO getAdminUserForFrontById(Long id) {
        AdminUser adminUser = baseMapper.selectById(id);
        FrontUserDTO frontUserDTO = new FrontUserDTO();
        frontUserDTO.setUsername(adminUser.getUsername());
        frontUserDTO.setEmail(adminUser.getEmail()==null? adminUser.getUsername() : adminUser.getEmail());
        frontUserDTO.setId(adminUser.getId());
        return frontUserDTO;
    }
}
