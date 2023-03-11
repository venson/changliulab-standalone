package com.venson.changliulabstandalone.service.admin;

import com.alibaba.fastjson.JSONObject;
import com.venson.changliulabstandalone.entity.pojo.AdminPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.pojo.AdminRolePermission;
import com.venson.changliulabstandalone.entity.dto.AdminPermissionDTO;
import com.venson.changliulabstandalone.entity.dto.MenuDTO;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface AdminPermissionService extends IService<AdminPermission> {


    //根据角色获取菜单
//    List<AdminPermissionDTO> doGetPermissionsByRoleId(Long roleId);



    //根据用户id获取用户菜单
    List<String> selectPermissionValueByUserId(Long id);

    List<JSONObject> selectPermissionByUserId(Long id);

    //获取全部菜单
    List<AdminPermissionDTO> doGetAllPermissionTree();

    //递归删除菜单
    void doRemovePermissionById(Long id);

    //给角色分配权限
    void saveRolePermissionRelationShipLab(Long roleId,Long[] permissionId);

    Map<Long,AdminRolePermission> getPermissionIdsByRoleId(Long id);

    List<AdminRolePermission> getRolePermissionByRoleId(Long id);

    void doUpdatePermission(Long id, AdminPermission adminPermission);

    List<MenuDTO> doGetMenus(Long id);

    List<Long> doGetPermissionsIdsByRoleId(Long roleId);

    List<Long> getIgnorePermissionIds();

    void addPermission(AdminPermissionDTO permission);

    List<Long> getMenuPermissionIdsByUserId(Long id);

    List<AdminPermission> getAllPermissions(boolean allPermission);
}
