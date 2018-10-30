package com.sf.rsa.common;

import java.util.List;

/**
 * 报文结果对象
 */
@SuppressWarnings("unchecked")
public final class R<T> extends AbstractResultMsg<T> {

    private R() {
        setSuccess(true);
        setCode(StatusEnum.SUCCESS.getValue());
        setMessage(StatusEnum.SUCCESS.getText());
    }

    private R(String message) {
        setSuccess(true);
        setCode(StatusEnum.SUCCESS.getValue());
        setMessage(message);
    }

    private R(boolean success, String message, String code) {
        setSuccess(success);
        setMessage(message);
        setCode(code);
    }

    private R(T data) {
        setSuccess(true);
        setCode(StatusEnum.SUCCESS.getValue());
        setMessage(StatusEnum.SUCCESS.getText());
        this.setData(data);
    }

    public R(boolean success, String message, String code, T data) {
        setSuccess(success);
        setMessage(message);
        setCode(code);
        setData(data);
    }

    public static R success() {
        return new R<>();
    }

    public static R success(String message) {
        return new R<>(message);
    }

    public static R success(String message, String code) {
        return new R<>(true, message, code);
    }

    public static <T> R<T> success(T data) {
        return new R<>(data);
    }

    public static <T> R<List<T>> success(List<T> data) {
        return new R<>(data);
    }

    public static R error(String message) {
        return new R<>(false, message, StatusEnum.ERROR.getValue());
    }

    public static R error(String message, String code) {
        return new R<>(false, message, code);
    }

}
