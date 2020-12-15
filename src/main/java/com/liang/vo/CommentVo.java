package com.liang.vo;

import com.liang.entity.Comment;
import lombok.Data;

import java.util.List;

/**
 * @author LiangYonghui
 * @date 2020/10/15 19:03
 * @description
 */
@Data
public class CommentVo {
    private Comment comment; // 评论的id
    List<Comment> subComments;
}
