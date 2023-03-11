package com.venson.changliulabstandalone.service.front;

import com.baomidou.mybatisplus.extension.service.IService;
import com.venson.changliulabstandalone.entity.FrontUser;
import com.venson.changliulabstandalone.entity.vo.FrontUserResetPasswordVo;
import com.venson.changliulabstandalone.entity.vo.RegistrationVo;
import com.venson.changliulabstandalone.entity.vo.front.FrontUserDTO;


/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author venson
 * @since 2022-05-24
 */
public interface FrontUserService extends IService<FrontUser> {


    void register(RegistrationVo vo);

//    List<FrontUser> getMemberList(String filter);

    FrontUser selectByUsername(String username);


    void resetPassword(FrontUserResetPasswordVo vo);

    FrontUserDTO getFrontUserById(Long userId);

}
