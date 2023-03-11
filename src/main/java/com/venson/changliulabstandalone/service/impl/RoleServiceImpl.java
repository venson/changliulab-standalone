package com.venson.changliulabstandalone.service.impl;

import com.venson.changliulabstandalone.constant.AdminCacheConst;
import com.venson.changliulabstandalone.entity.pojo.AdminRole;
import com.venson.changliulabstandalone.entity.pojo.AdminRolePermission;
import com.venson.changliulabstandalone.entity.pojo.AdminUserRole;
import com.venson.changliulabstandalone.entity.dto.AdminRolePermissionDTO;
import com.venson.changliulabstandalone.mapper.AdminRoleMapper;
import com.venson.changliulabstandalone.service.admin.AdminPermissionService;
import com.venson.changliulabstandalone.service.admin.AdminRolePermissionService;
import com.venson.changliulabstandalone.service.admin.AdminRoleService;
import com.venson.changliulabstandalone.service.admin.AdminUserRoleService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
@Service
public class RoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements AdminRoleService {

    @Autowired
    private AdminUserRoleService adminUserRoleService;
    @Autowired
    private AdminPermissionService permissionService;
    @Autowired
    private AdminRolePermissionService rolePermissionService;


    //根据用户获取角色数据
//    @Override
//    public Map<String, Object> findRoleByUserId(Long userId) {
//        //查询所有的角色
//        List<AdminRole> allRolesList =baseMapper.selectList(null);
//
//        //根据用户id，查询用户拥有的角色id
//        List<AdminUserRole> existUserRoleList = adminUserRoleService.list(new QueryWrapper<AdminUserRole>().eq("user_id", userId).select("role_id"));
//
//        List<Long> existRoleList = existUserRoleList.stream().map(AdminUserRole::getRoleId).toList();
//
//        //对角色进行分类
//        List<AdminRole> assignAdminRoles = new ArrayList<>();
//        for (AdminRole adminRole : allRolesList) {
//            //已分配
//            if(existRoleList.contains(adminRole.getId())) {
//                assignAdminRoles.add(adminRole);
//            }
//        }
//
//        Map<String, Object> roleMap = new HashMap<>();
//        roleMap.put("assignRoles", assignAdminRoles);
//        roleMap.put("allRolesList", allRolesList);
//        return roleMap;
//    }

    //根据用户分配角色
    @Override
    public void saveUserRoleRelationShip(Long userId,Long[] roleIds) {
        adminUserRoleService.remove(new QueryWrapper<AdminUserRole>().eq("user_id", userId));

        List<AdminUserRole> userRoleList = new ArrayList<>();
        for(Long roleId : roleIds) {
            if(ObjectUtils.isEmpty(roleId)) continue;
            AdminUserRole userRole = new AdminUserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);

            userRoleList.add(userRole);
        }
        adminUserRoleService.saveBatch(userRoleList);
    }

    @Override
    public List<AdminRole> selectRoleByUserId(Long id) {
        //根据用户id拥有的角色id
        List<AdminUserRole> userRoleList = adminUserRoleService.list(new QueryWrapper<AdminUserRole>().eq("user_id", id).select("role_id"));
        List<Long> roleIdList = userRoleList.stream().map(AdminUserRole::getRoleId).collect(Collectors.toList());
        List<AdminRole> adminRoleList = new ArrayList<>();
        if(roleIdList.size() > 0) {
            adminRoleList = baseMapper.selectBatchIds(roleIdList);
        }
        return adminRoleList;
    }

    @Override
//    @CacheEvict(value = {AdminCacheConst.USER_MENU_NAME, AdminCacheConst.USER_MENU_NAME}, allEntries = true)
    public void addRoleWithPermissions(AdminRolePermissionDTO rolePermissionDTO) {
        AdminRole role = new AdminRole();
        BeanUtils.copyProperties(rolePermissionDTO,role);
        baseMapper.insert(role);
        permissionService.saveRolePermissionRelationShipLab(role.getId(),
                rolePermissionDTO.getPermissionIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = {AdminCacheConst.USER_MENU_NAME, AdminCacheConst.USER_MENU_NAME}, allEntries = true)
    public void updateRoleWithPermissions(AdminRolePermissionDTO rolePermissionDTO) {
        AdminRole role = baseMapper.selectById(rolePermissionDTO.getId());
        Assert.notNull(role);
        // update role information
        role.setRoleName(role.getRoleName());
        role.setRoleCode(role.getRoleCode());
        role.setRemark(role.getRemark());
        baseMapper.updateById(role);
        // update role permission relation
        if(rolePermissionDTO.getPermissionChanged()){
            List<AdminRolePermission> addList= new ArrayList<>();
            HashSet<Long> permissionIdSet = new HashSet<>(List.of(rolePermissionDTO.getPermissionIds()));
            Map<Long, AdminRolePermission> permissionIdRoleMap = permissionService.getPermissionIdsByRoleId(role.getId());
            for (Long permissionId : permissionIdSet) {
                if (permissionIdRoleMap.containsKey(permissionId)) {
                    permissionIdRoleMap.remove(permissionId);
                    // add the id of adminRolePermission to removeList
                } else {
                    // add new AdminRolePermission to addList
                    AdminRolePermission rolePermission = new AdminRolePermission(role.getId(), permissionId);
                    addList.add(rolePermission);

                }
            }
            List<Long> removeList=permissionIdRoleMap.values().stream()
                    .map(AdminRolePermission::getId)
                    .collect(Collectors.toList());
            if(addList.size()>0){
                rolePermissionService.saveBatch(addList);
            }

            if(removeList.size()>0){
                rolePermissionService.removeBatchByIds(removeList);
            }
        }

    }

    @Override
    @CacheEvict(value = {AdminCacheConst.USER_MENU_NAME, AdminCacheConst.USER_MENU_NAME}, allEntries = true)
    public void removeRoleById(Long id) {
        baseMapper.deleteById(id);
    }
}
