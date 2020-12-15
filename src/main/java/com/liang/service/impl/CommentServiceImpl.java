package com.liang.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.liang.common.lang.Result;
import com.liang.dto.AddCommentDto;
import com.liang.entity.Article;
import com.liang.entity.Comment;
import com.liang.entity.Message;
import com.liang.entity.User;
import com.liang.mapper.ArticleMapper;
import com.liang.mapper.CommentMapper;
import com.liang.mapper.MessageMapper;
import com.liang.mapper.UserMapper;
import com.liang.service.CommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liang.utils.BlogConstant;
import com.liang.utils.RedisKeyUtil;
import com.liang.vo.CommentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private RedisTemplate redisTemplate;



    @Override
    public Result addComment(AddCommentDto addCommentDto) {
        System.out.println("addCommentDto--->" + addCommentDto);
        // 为新的评论赋值
        Comment comment = new Comment();
        comment.setArticleId(addCommentDto.getArticleId());
        comment.setContent(addCommentDto.getContent());
        comment.setNickname(addCommentDto.getObserverName());
        comment.setFatherComment(addCommentDto.getParentId());
        comment.setTime(new Date());
        comment.setUserId(addCommentDto.getUserId());
        comment.setPollCount(0);
        User user = userMapper.selectById(addCommentDto.getToUserId());
        comment.setToNickname(user.getNickname());
        comment.setToUserId(addCommentDto.getToUserId());
        System.out.println("------------------------------------");
        System.out.println(comment);
        commentMapper.insert(comment);
        Article article = articleMapper.selectById(addCommentDto.getArticleId());

        article.setCommentCount(article.getCommentCount()+1);
        articleMapper.updateById(article);

        //缓存评论的数量====================
//        redisTemplate.execute(new SessionCallback() {
//            @Override
//            public Object execute(RedisOperations operations) throws DataAccessException {
//                String entityCommentKey = RedisKeyUtil.getEntityCommentKey(BlogConstant.ENTITY_TYPE_ARTICLE
//                        , article.getId());
//
//
//                boolean isMember = operations.opsForSet().isMember(entityCommentKey, addCommentDto.getUserId());
//
//                operations.multi();
//
//                if (isMember) {
//                    operations.opsForSet().remove(entityCommentKey, addCommentDto.getUserId());
//                } else {
//                    operations.opsForSet().add(entityCommentKey, addCommentDto.getUserId());
//                }
//
//                return operations.exec();
//            }
//        });


        //=======================


        ExecutorService executorService = Executors.newFixedThreadPool(1);

        Runnable runnable = new Runnable(){

            @Override
            public void run() {
                // 发送消息，后续最好换成异步
                Message message = new Message();
                message.setFromId(addCommentDto.getUserId());
                message.setToId(addCommentDto.getToUserId());
                message.setStatus(0);
                // 用于鉴定是评论文章还是回复评论还是别的什么
                message.setConversationId(BlogConstant.TOPIC_COMMENT);
                message.setCreateTime(new Date());

                Map<String, Object> content = new HashMap<>();


                // 获取文章名
                User sender = userMapper.selectById(addCommentDto.getUserId());
                // 设置评论发起者
                content.put("senderName", sender.getNickname());

                // 如果没有父级评论，就是评论文章
                if (addCommentDto.getParentId() == null || "".equals(addCommentDto.getParentId())) {
                    content.put("entityType",BlogConstant.ENTITY_TYPE_ARTICLE);
                    content.put("articleName",article.getTitle());
                } else {
                    //回复评论
                    content.put("entityType",BlogConstant.ENTITY_TYPE_COMMENT);

                    // 获取父级评论
                    Comment fatherComment = commentMapper.selectById(addCommentDto.getParentId());
                    content.put("fatherComment",fatherComment.getContent());
                }

                // 设置文章id
                content.put("articleId",article.getId());
                // 设置当前评论或者回复的id
                content.put("commentOrReplyId",comment.getId());
                // 设置评论或回复内容
                content.put("commentText",addCommentDto.getContent());

                message.setContent(JSONObject.toJSONString(content));

                System.out.println( "---------------------" +message + "---------------------");

                messageMapper.insert(message);
            }
        };

        executorService.submit(runnable);

        return Result.success("评论成功！");
    }

    @Override
    public Result getArticleComments(Long articleId) {
        // 根据文章id查询第一层评论
        HashMap<String, Object> commentMap = new HashMap<>();
        commentMap.put("article_id",articleId);
        commentMap.put("father_comment",null);
        QueryWrapper<Comment> commentWrapper = new QueryWrapper<>();
        commentWrapper.allEq(commentMap).orderByDesc("time");

        List<Comment> comments = commentMapper.selectList(commentWrapper);
        for (Comment comment : comments) {
            System.out.println("comment-->" + comment);
        }
        List<CommentVo> res = eachcomment(comments);
        System.out.println("commentVos--->" + res);
        return Result.success(res);
    }


    // 先装第一层到结果集中
    private List<CommentVo> eachcomment(List<Comment> comments) {
        List<CommentVo> commentVos = new ArrayList<>();

        for (Comment comment : comments) {
            CommentVo commentVo = new CommentVo();
            commentVo.setComment(comment);
            commentVos.add(commentVo);
        }

        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentVos);



        return commentVos;
    }

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    // 给每一个第一层的评论装他们的后代评论
    private void combineChildren(List<CommentVo> commentVos) {
        for (CommentVo commentVo : commentVos) {
            //循环迭代，找出子代，存放在tempReplys中
            recursively(commentVo.getComment());

            //修改顶级节点的reply集合为迭代处理后的集合
            commentVo.setSubComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }

    }




    /**
     * 递归迭代，剥洋葱
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("father_comment",comment.getId());
        QueryWrapper<Comment> commentWrapper = new QueryWrapper<>();
        commentWrapper.allEq(map).orderByDesc("time");
        List<Comment> comments = commentMapper.selectList(commentWrapper);
        if (comments.size() > 0) {
            for (Comment subComment : comments) {
                tempReplys.add(subComment);
                // 查询子代的子代
                HashMap<String, Object> subMap = new HashMap<>();
                subMap.put("father_comment",subComment.getId());
                QueryWrapper<Comment> commentWrapper1 = new QueryWrapper<>();
                commentWrapper1.allEq(subMap).orderByDesc("time");
                List<Comment> comments1 = commentMapper.selectList(commentWrapper1);
                if (comments.size() > 0) {
                    for (Comment comment1 : comments1) {
                        tempReplys.add(comment1);
                        recursively(comment1);
                    }
                }
            }
        }


    }


}
