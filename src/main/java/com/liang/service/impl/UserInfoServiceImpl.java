package com.liang.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liang.common.lang.Result;
import com.liang.entity.UserInfo;
import com.liang.mapper.UserInfoMapper;
import com.liang.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liang.utils.QiNiuConst;
import com.liang.vo.SettingVo;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
@Service
@Transactional
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService,QiNiuConst {

    @Autowired
    private UserInfoMapper userInfoMapper;
    // qjavcyfnj.hn-bkt.clouddn.com


    @Override
    public Result getSetting(Long userId) {
        System.out.println("userId---------->"+userId);
//        // 上传文件名称
        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString();

        // 生成上传凭证
        Auth auth = Auth.create(QiNiuConst.ACCESS_KEY, QiNiuConst.SECRET_KEY);
        String uploadToken = auth.uploadToken(QiNiuConst.AVATAR_BUCKET_NAME, fileName);


        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);

        SettingVo settingVo = new SettingVo();
        settingVo.setUserInfo(userInfo);
        settingVo.setFileName(fileName);
        settingVo.setUploadToken(uploadToken);
        System.out.println(uploadToken);
        System.out.println(settingVo);





        userInfoMapper.updateById(userInfo);

        return Result.success(settingVo);
    }

    @Override
    public Result updateUrl(String url, Long userId) {
        System.out.println("=====================");
        System.out.println(url);
        System.out.println(userId);
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        userInfo.setAvatarUrl(url);
        userInfoMapper.updateById(userInfo);
        return Result.success();
    }

    @Override
    public Result getUrlById(long userId) {
        System.out.println("----e900e3908r9040893893840982394832948320");
        QueryWrapper<UserInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userId);
        UserInfo userInfo = userInfoMapper.selectOne(wrapper);
        return Result.success(userInfo);
    }
}
