package com.venson.changliulab.service.admin;

import com.venson.changliulab.entity.pojo.AdminUserRole;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface AdminUserRoleService extends IService<AdminUserRole> {

    void updateUserRole(Long id, List<Long> roleIds);
}
