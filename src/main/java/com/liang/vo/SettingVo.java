package com.liang.vo;

import com.liang.entity.Blog;
import com.liang.entity.UserInfo;
import lombok.Data;

/**
 * @author LiangYonghui
 * @date 2020/11/5 9:06
 * @description
 */
@Data
public class SettingVo {
    private UserInfo userInfo;
    private String uploadToken; // 上传凭证
    private String fileName;
}
