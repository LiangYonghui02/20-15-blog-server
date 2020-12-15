package com.liang.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liang.common.lang.Result;
import com.liang.dto.UserDto;
import com.liang.entity.Blog;
import com.liang.entity.StatisInfo;
import com.liang.entity.User;
import com.liang.entity.UserInfo;
import com.liang.mapper.BlogMapper;
import com.liang.mapper.StatisInfoMapper;
import com.liang.mapper.UserInfoMapper;
import com.liang.mapper.UserMapper;
import com.liang.service.BlogService;
import com.liang.service.UserInfoService;
import com.liang.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liang.shiro.JwtToken;
import com.liang.utils.JwtUtil;
import com.liang.utils.QiNiuConst;
import com.liang.utils.RegexUtils;
import com.liang.vo.UserMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.awt.event.PaintEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
@Service
@Slf4j
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    HttpServletRequest req;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserInfoMapper userInfoMapper;

    @Autowired
    BlogMapper blogMapper;

    @Autowired
    StatisInfoMapper statisInfoMapper;

    private String logTem = "[user/register]==>";
    private String logTem2 = "[user/ver_code]==>";
    // 获取验证码的key
    private static final String VERCODE_SESSION_KEY = "VERCODE_SESSION_KEY";

    @Override
    public Result register(UserDto userDto) {
        if (StrUtil.hasEmpty(userDto.getNickname(),userDto.getPassword(),userDto.getVercode())) {
            return Result.fail("表单项不能为空。");
        }
        // 查看当前邮箱是否已经注册
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("email",userDto.getEmail());
        User dbUser = userMapper.selectOne(wrapper);
        if (dbUser != null) {
            return Result.fail("该邮箱已经被注册。");
        }


        String verCode = (String)  req.getSession().getAttribute(VERCODE_SESSION_KEY);
        log.info(logTem + "verCode-->{" + userDto.getVercode() + "}");
        log.info(logTem + "userDto-->{" + userDto.toString() + "}");
        System.out.println(verCode);
//        if(userDto.getVercode() == null || !userDto.getVercode().equalsIgnoreCase(verCode)) {
//            log.info(logTem + "vercode err" );
//            return Result.fail("验证码输入不正确");
//        }
        User saveUser = new User();
        saveUser.setEmail(userDto.getEmail());
        saveUser.setNickname(userDto.getNickname());
        saveUser.setPassword(SecureUtil.md5(userDto.getPassword()));
        userMapper.insert(saveUser);

        System.out.println(saveUser + "-----------------------------");

        // 放到信息详情中
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(saveUser.getEmail());
        userInfo.setNickname(saveUser.getNickname());
        userInfo.setUserId(saveUser.getId());
        userInfo.setSignature("这家伙很懒，没有签名。");
        userInfo.setIntro("无");
        userInfo.setAvatarUrl(QiNiuConst.AVATAR_URL_PR + "F.png");
        userInfoMapper.insert(userInfo);

        // 分配博客地址
        Blog blog = new Blog();
        blog.setUserId(saveUser.getId());
        blog.setBlogUrl("mine/"+saveUser.getId());
        blogMapper.insert(blog);


        // 分配数据
        StatisInfo statisInfo = new StatisInfo();
        statisInfo.setArticleCount(0);
        statisInfo.setAttention(0);
        statisInfo.setFans(0);
        statisInfo.setWords(0);
        statisInfo.setLikeCount(0);
        statisInfo.setUserId(saveUser.getId());
        statisInfoMapper.insert(statisInfo);


        return Result.success("注册成功，可以去登录了。");
    }

    @Override
    public Result createVerCode(String email) {
        if (!RegexUtils.isEmail(email)) {
            return Result.fail("邮箱不符合规则");
        }
        String verCode = RandomUtil.randomNumbers(6);
        req.getSession().setAttribute(VERCODE_SESSION_KEY,verCode);
        log.info(logTem2 + " verCode-->{" + verCode + "}" );
        ArrayList<String> tos = CollUtil.newArrayList(email);
        MailUtil.send(tos, "FAFY-blog", "验证码：<b>"+verCode+"</b> <br> 消息来自：<span>042工作室</span>", true);

        return Result.success("验证码已发送，注意查看邮箱。");
    }

    @Override
    public Result login(User user) {
        if (StrUtil.hasEmpty(user.getEmail(),user.getPassword())) {
            return Result.fail("表单项不能为空");
        }

        System.out.println(user);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        HashMap<String, String> map = new HashMap<>();
        map.put("email",user.getEmail());
        map.put("password",SecureUtil.md5(user.getPassword()));
        wrapper.eq("email",user.getEmail()).allEq(map);
        User dbUser = userMapper.selectOne(wrapper);
        if (ObjectUtil.isNull(dbUser)) {
            return Result.fail("登陆失败！邮箱或密码错误。");
        }

        // 查找详细信息
        QueryWrapper<UserInfo> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.eq("user_id",dbUser.getId());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoWrapper);
        System.out.println("dbuser---->" + dbUser);
        System.out.println("userInfo---->" + userInfo);

        //生成token
        Map<String,String> tokenMap = new HashMap<>();
        tokenMap.put("nickname",dbUser.getNickname());
        tokenMap.put("id",dbUser.getId()+"");
        tokenMap.put("email",dbUser.getEmail());
        JwtToken jwToken = new JwtToken(JwtUtil.getToken(tokenMap,dbUser.getEmail()));
        try {//让shiro缓存用户信息
            SecurityUtils.getSubject().login(jwToken);
        } catch (UnknownAccountException e) {
            return Result.fail("用户不存在。");
        } catch (IncorrectCredentialsException e) {
            return Result.fail("账号或密码错误");
        }



        UserMsg userMsg = new UserMsg();

        userMsg.setUserInfo(userInfo);

        userMsg.setJwtCert(jwToken.getCredentials());

        return Result.success("登陆成功",userMsg);

    }
}
