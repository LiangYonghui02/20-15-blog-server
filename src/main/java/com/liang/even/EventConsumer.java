//package com.liang.even;
//
//import com.alibaba.fastjson.JSONObject;
//import com.liang.entity.Article;
//import com.liang.entity.Event;
//import com.liang.entity.Message;
//import com.liang.entity.User;
//import com.liang.service.ArticleService;
//import com.liang.service.MessageService;
//import com.liang.service.UserService;
//import com.liang.utils.BlogConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @author LiangYonghui
// * @date 2020/11/1 9:12
// * @description
// */
//@Component
//@Slf4j
//public class EventConsumer implements BlogConstant {
//
//    @Autowired
//    MessageService messageService;
//
//    @Autowired
//    UserService userService;
//
//    @Autowired
//    ArticleService articleService;
//
//    @KafkaListener(topics = {TOPIC_COMMENT, TOPIC_LIKE, TOPIC_FOLLOW,TOPIC_COLLECT})
//    public void handleCommentMessage(ConsumerRecord record) {
//        if (record == null || record.value() == null) {
//            log.info("消费信息信息为空");
//            return;
//        }
//
//        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
//        if (event == null) {
//            log.error("消息格式错误!");
//            return;
//        }
//
//        // 发送站内通知
//        Message message = new Message();
//        message.setFromId(event.getUserId());
//        message.setToId(event.getEntityUserId());
//        // 用于鉴定是收藏还是别的什么
//        message.setConversationId(event.getTopic());
//        message.setCreateTime(new Date());
//
//        Map<String, Object> content = new HashMap<>();
//
//        if (event.getTopic() == (BlogConstant.TOPIC_COLLECT)) {
//            System.out.println("=============BlogConstant.TOPIC_COLLECT==============");
//            // 获取文章名
//            Article article = articleService.getById(event.getEntityId());
//            // 收藏发起者
//            User user = userService.getById(event.getUserId());
//            content.put("articleName",article.getTitle());
//            content.put("collectUser",user.getNickname());
//            message.setContent(JSONObject.toJSONString(content));
//
//            messageService.save(message);
//        }
//
//    }
//}
