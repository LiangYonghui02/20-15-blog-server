package com.liang.utils;

import com.liang.common.lang.Result;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LiangYonghui
 * @date 2020/10/11 16:22
 * @description
 */
public class RegexUtils {

    public final static boolean isEmail(String email) {
        Pattern p = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\\.([a-zA-Z0-9_-])+)+$");
        if (null== email || !p.matcher(email).matches()) {
            return false;
        }
        return true;
    }
}
