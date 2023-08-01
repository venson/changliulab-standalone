package com.venson.changliulab.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.venson.changliulab.entity.pojo.AdminUserRole;
import com.venson.changliulab.mapper.AdminUserRoleMapper;
import com.venson.changliulab.service.admin.AdminUserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class AdminUserRoleServiceImpl extends ServiceImpl<AdminUserRoleMapper, AdminUserRole> implements AdminUserRoleService {

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserRole(Long userId, List<Long> roleIds) {
        LambdaQueryWrapper<AdminUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AdminUserRole::getUserId,userId).select(AdminUserRole::getRoleId);
        List<Long> oldRoleIds= this.listObjs(wrapper, (o) -> Long.valueOf(o.toString()));
        List<Long> commonRoleIds = roleIds.stream().filter(oldRoleIds::contains).toList();
        oldRoleIds.removeAll(commonRoleIds);
        roleIds.removeAll(commonRoleIds);
        if(oldRoleIds.size()!=0){
            wrapper.clear();
            wrapper.eq(AdminUserRole::getUserId,userId).in(AdminUserRole::getRoleId,oldRoleIds);
            baseMapper.delete(wrapper);
        }
        if(roleIds.size()!=0){
            List<AdminUserRole> newUserRoleList= new ArrayList<>();
            roleIds.forEach((roleId) ->{
                AdminUserRole userRole = new AdminUserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(roleId);
                newUserRoleList.add(userRole);
            });
            this.saveBatch(newUserRoleList);
        }


    }
}
