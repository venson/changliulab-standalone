package com.venson.changliulab.service.admin;

import com.venson.changliulab.entity.pojo.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulab.entity.dto.AclUserDTO;
import com.venson.changliulab.entity.vo.UserPersonalVO;
import com.venson.changliulab.entity.vo.admin.PageQueryVo;
import com.venson.changliulab.entity.vo.front.FrontUserDTO;
import com.venson.changliulab.utils.PageResponse;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author testjava
 * @since 2020-01-12
 */
public interface AdminUserService extends IService<AdminUser> {

    // 从数据库中取出用户信息
    AdminUser selectByUsername(String username);

    void resetRandomPasswordById(Long id);

    void updateAclUser(AclUserDTO user);

    void updateUserPersonalInfo(UserPersonalVO UserPersonalVO, Long userId);

//    void resetPasswordForUser();

    AclUserDTO getUserById(Long id);

    void addUser(AclUserDTO user);

    void removeUserById(Long id);

    FrontUserDTO getAdminUserForFrontById(Long id);

    PageResponse<AdminUser> getPage(PageQueryVo vo);
}
