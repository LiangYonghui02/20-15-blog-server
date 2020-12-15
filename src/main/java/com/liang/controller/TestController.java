package com.liang.controller;

import com.liang.annotation.TokenFree;
import com.liang.config.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author LiangYonghui
 * @date 2020/11/7 21:29
 * @description
 */
@RestController
@RequestMapping("/api")
public class TestController {
    @Autowired
    private WebSocket webSocket;

    @TokenFree
    @RequestMapping("/sendAllWebSocket")
    public String test() {
        webSocket.sendAllMessage("清晨起来打开窗，心情美美哒~");
        return "websocket群体发送！";
    }

    @TokenFree
    @RequestMapping("/sendOneWebSocket")
    public String sendOneWebSocket() {
        webSocket.sendOneMessage("13151859292369018891317058934183366658", "只要你乖给你买条gai！");
        return "websocket单人发送";
    }
}