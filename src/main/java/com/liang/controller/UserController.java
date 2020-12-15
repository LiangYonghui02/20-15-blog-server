package com.liang.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.mail.MailUtil;
import com.liang.annotation.TokenFree;
import com.liang.common.lang.Result;
import com.liang.dto.UserDto;
import com.liang.entity.User;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {
    @TokenFree
    @GetMapping("/ver_code")
    public Result createVerCode(@RequestParam String email) {
        return userService.createVerCode(email);
    }

    @TokenFree
    @PostMapping("/register")
    public Result register(@RequestBody(required = false) UserDto userDto) {
        return userService.register(userDto);
    }


    @TokenFree
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        return userService.login(user);
    }

}
