package com.liang.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liang.entity.Message;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LiangYonghui
 * @date 2020/11/2 15:17
 * @description
 */
@Repository
public interface MessageMapper extends BaseMapper<Message> {

    List<String> selectLastMsgDate(@Param("userId") Long userId);
}
