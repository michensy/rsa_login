package com.sf.rsa.common;

import java.io.Serializable;


public abstract class AbstractResultMsg<T> implements Serializable {

    protected String code;

    protected String message;

    protected Boolean success = Boolean.TRUE;

    protected T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}