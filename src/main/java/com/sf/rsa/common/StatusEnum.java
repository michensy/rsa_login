package com.sf.rsa.common;


/**
 * 通用的正确返回枚举类
 */

public enum StatusEnum {
    SUCCESS("0", "操作成功"),
    ERROR("-1", "服务异常");

    /**
     * 值
     */
    private String value;
    /**
     * 中文描述
     */
    private String text;


    StatusEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * @return 当前枚举对象的值。
     */
    public String getValue() {
        return value;
    }

    /**
     * @return 当前状态的中文描述。
     */
    public String getText() {
        return text;
    }


}
