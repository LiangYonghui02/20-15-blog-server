package com.liang.service;

import com.liang.common.lang.Result;
import com.liang.dto.UserDto;
import com.liang.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
public interface UserService extends IService<User> {

    Result register(UserDto userDto);

    Result createVerCode(String email);

    Result login(User user);
}
