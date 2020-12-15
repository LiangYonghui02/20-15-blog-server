package com.liang.common.lang;

import com.liang.exception.CustomException;
import com.liang.exception.CustomExceptionCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    // 24成功，-1失败
    private int status;
    private String msg;
    private Object data;
    private String action;

    public static Result success() {
        return Result.success("操作成功", null);
    }

    public static Result success(Object data) {
        return Result.success("操作成功", data);
    }

    public static Result success(String msg, Object data) {
        Result result = new Result();
        result.status = 200;
        result.msg = msg;
        result.data = data;
        return result;
    }

    public static Result success(String msg) {
        Result result = new Result();
        result.status = 200;
        result.msg = msg;
        return result;
    }

    public static Result fail(String msg) {
        Result result = new Result();
        result.status = -1;
        result.data = null;
        result.msg = msg;
        return result;
    }

    public Result action(String action){
        this.action = action;
        return this;
    }

    public Result(String msg) {
        this.msg = msg;
    }

    public Result(CustomException e) {
        this.status = e.getCode();
        this.msg = e.getMessage();
    }

    public Result() {
        this.status = 200;
    }

    public Result(CustomExceptionCode e) {
        this.status = e.getCode();
        this.msg = e.getMessage();
    }
}
