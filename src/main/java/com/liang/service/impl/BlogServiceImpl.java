package com.liang.service.impl;

import com.liang.entity.Blog;
import com.liang.mapper.BlogMapper;
import com.liang.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author LiangYonghui
 * @since 2020-10-10
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
