package com.liang.dto;

import lombok.Data;

/**
 * @author LiangYonghui
 * @date 2020/10/31 17:49
 * @description
 */
@Data
public class ReadDto {
    Long userId; // 用户Id
    Long entityId; // 实体的Id
    Long entityUserId; // 实体所有者的Id
    Integer entityType; // 实体类型
}
