package com.venson.changliulab.service.admin;

import com.venson.changliulab.entity.dto.AdminPermissionTree;
import com.venson.changliulab.entity.pojo.AdminPermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.pojo.AdminRolePermission;
import com.venson.changliulab.entity.dto.AdminPermissionDTO;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.utils.PageResponse;

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

    /**
     * return list of slug for SecurityUser
     * @param id user Id
     * @return return list of slug
     */
    List<String> selectPermissionValueByUserId(Long id);

//    List<JSONObject> selectPermissionByUserId(Long id);

    //获取全部菜单
//    List<AdminPermissionDTO> doGetAllPermissionTree();

    //递归删除菜单
    AdminPermission doRemovePermissionById(Long id);

    //给角色分配权限
    void saveRolePermissionRelationShipLab(Long roleId, List<Long> permissionId);

    Map<Long,AdminRolePermission> getPermissionIdsByRoleId(Long id);

    List<AdminRolePermission> getRolePermissionByRoleId(Long id);

    void doUpdatePermission(Long id, AdminPermission adminPermission);

//    List<MenuDTO> doGetMenus(Long id);

    List<Long> doGetPermissionsIdsByRoleId(Long roleId);

//    List<Long> getIgnorePermissionIds();

    Long addPermission(AdminPermissionDTO permission);

//    List<PermissionDTO> getPermissionsByUserId(Long id);

//    List<AdminPermission> getAllPermissions(boolean allPermission);

    PageResponse<AdminPermission> getPage(PageQueryVo vo);

    List<AdminPermissionTree> doGetAllPermission();
}
