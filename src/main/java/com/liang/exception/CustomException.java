package com.liang.exception;

/**
 * @author LiangYonghui
 * @date 2020/10/11 9:25
 * @description
 */
public class CustomException extends RuntimeException {
    private String message;
    private Integer code ;

    public CustomException(final ICustomExceptionCode customExceptionCode) {
        this.code = customExceptionCode.getCode();
        this.message = customExceptionCode.getMessage();
    }

    public CustomException(String message) {
        super(message);
        this.code = -1;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
