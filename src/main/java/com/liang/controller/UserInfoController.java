package com.liang.controller;


import cn.hutool.core.lang.UUID;
import com.liang.common.lang.Result;
import com.liang.service.UserInfoService;
import com.liang.utils.QiNiuConst;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
@RestController
@RequestMapping("/user_info")
public class UserInfoController extends BaseController implements QiNiuConst {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private HttpServletRequest req;

    @GetMapping("get_setting")
    public Result setting() {
        Long userId = getUserId(req);
        return  userInfoService.getSetting(userId);

    }


    @PostMapping("/update_url")
    public Result updateUrl(@RequestBody HashMap<String,String> map) {
        Long userId = getUserId(req);
        String url = map.get("url");
        return userInfoService.updateUrl(url,userId);
    }


    @GetMapping("/get_url/{id}")
    public Result getUrlById(@PathVariable String id) {
        long userId = Long.parseLong(id);
        return userInfoService.getUrlById(userId);
    }


}
