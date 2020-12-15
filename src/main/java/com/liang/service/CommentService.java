package com.liang.service;

import com.liang.common.lang.Result;
import com.liang.dto.AddCommentDto;
import com.liang.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
public interface CommentService extends IService<Comment> {

    Result addComment(AddCommentDto addCommentDto);

    Result getArticleComments(Long articleId);
}
