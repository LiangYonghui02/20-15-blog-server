package com.liang;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.liang.config.MailConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class FafyBlogApplicationTests {
    @Autowired
    private MailConfig mailConfig;

    @Test
    void contextLoads() {
        String s = RandomUtil.randomNumbers(6);
        ArrayList<String> tos = CollUtil.newArrayList(
                "431996434@qq.com");
        MailUtil.send(tos, "FAFY-blog", "验证码：<b>123456</b> <br> from <span style='color: #ffaa50'>042工作室。</span>", true);
    }

}
