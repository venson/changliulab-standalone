package com.venson.changliulabstandalone.service.front;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.FrontPermission;

import java.util.List;

/**
 * <p>
 * 权限 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface FrontPermissionService extends IService<FrontPermission> {

    List<String> selectPermissionValueByUserId(Long id);
}
