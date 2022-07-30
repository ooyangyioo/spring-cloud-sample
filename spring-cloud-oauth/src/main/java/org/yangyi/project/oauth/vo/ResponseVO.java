package org.yangyi.project.oauth.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseVO<T> {

    private static final String DEFAULT_SUCCESS_CODE = "1";
    private static final String DEFAULT_FAILED_CODE = "0";

    private static final String DEFAULT_SUCCESS_MESSAGE = "成功";
    private static final String DEFAULT_FAILED_MESSAGE = "失败";

    private String code;
    private String msg;
    private T data;

    public ResponseVO(String code, String msg) {
        this(code, msg, null);
    }

    public ResponseVO(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseVO success(T data) {
        return new ResponseVO(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, data);
    }

    public static <T> ResponseVO success(String message, T data) {
        return new ResponseVO(DEFAULT_SUCCESS_CODE, message, data);
    }

    public static ResponseVO success() {
        return new ResponseVO(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> ResponseVO failed(T data) {
        return new ResponseVO(DEFAULT_FAILED_CODE, DEFAULT_FAILED_MESSAGE, data);
    }

    public static <T> ResponseVO failed(String message, T data) {
        return new ResponseVO(DEFAULT_FAILED_CODE, message, data);
    }

    public static ResponseVO failed(String message) {
        return new ResponseVO(DEFAULT_FAILED_CODE, message);
    }

    public static ResponseVO failed() {
        return new ResponseVO(DEFAULT_FAILED_CODE, DEFAULT_FAILED_MESSAGE);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
