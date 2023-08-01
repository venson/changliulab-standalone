package com.venson.changliulab.service.admin;

import com.venson.changliulab.entity.pojo.AdminRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.dto.AdminRoleDTO;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.utils.PageResponse;

import java.util.List;
//import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface AdminRoleService extends IService<AdminRole> {

    //根据用户获取角色数据
//    Map<String, Object> findRoleByUserId(Long userId);

    //根据用户分配角色
    void saveUserRoleRelationShip(Long userId,Long [] roleId);

    List<AdminRole> selectRoleByUserId(Long id);

    Long addRoleWithPermissions(AdminRoleDTO rolePermissionDTO);

    void updateRoleWithPermissions(AdminRoleDTO rolePermissionDTO);

    void removeRoleById(Long id);

    PageResponse<AdminRole> getPage(PageQueryVo vo);

    AdminRoleDTO getRoleById(Long id);
}
