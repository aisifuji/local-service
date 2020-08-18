package com.herocheer.zhsq.localservice.core.base;


/**
 * @Author: User
 * @Description: 返回类
 * @Version: 1.0
 */
public class RestResult<T> {

    public final static Integer HTTPCODE = 200;
    public final static Boolean SUCCESS = true;
    public final static Boolean FAIL = false;
    public final static String MSG_SUCCESS = "操作成功";
    public final static String MSG_FAIL = "操作失败";

    //http响应业务状态碼
    private Integer code;

    // 响应业务状态 0 成功， 1失败
    private Boolean success;

    // 响应消息
    private String msg;

    // 响应中的数据
    private T data;

    public RestResult(Integer code, Boolean success, String msg, T data) {
        this.code = code;
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public RestResult(Boolean success, String msg, T data) {
        this.code = HTTPCODE;
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public RestResult(T data) {
        this.code = HTTPCODE;
        this.success = SUCCESS;
        this.msg = MSG_SUCCESS;
        this.data = data;
    }

    public static <T> RestResult<T> build(Boolean success, String msg, T data) {
        return new RestResult<T>(success, msg, data);
    }
    /*
    /成功请求
     */
    public static <T> RestResult<T> success(T data) {
        return new RestResult<T>(data);
    }

    public static <T> RestResult<T> success() {
        return new RestResult<T>(SUCCESS, MSG_SUCCESS, null);
    }

    public static <T> RestResult<T> success(String msg) {
        return new RestResult<T>(SUCCESS, msg, null);
    }
    /*
    /异常请求
   */
    public static <T> RestResult<T> error() {
        return new RestResult<T>(500, FAIL, MSG_FAIL, null);
    }
    /*
    /自定义失败请求
   */
    public static <T> RestResult<T> fail() {
        return new RestResult<T>(HTTPCODE, FAIL, MSG_FAIL, null);
    }

    public static <T> RestResult<T> fail(String msg) {
        return new RestResult<T>(HTTPCODE, FAIL, msg, null);
    }
    public static <T> RestResult<T> fail(String msg,T data) {
        return new RestResult<T>(HTTPCODE, FAIL, msg, data);
    }
    public static <T> RestResult<T> build(Boolean success, String msg) {
        return new RestResult<T>(success, msg, null);
    }

    public static <T> RestResult<T> build(Integer code, Boolean success, String msg, T data) {
        return new RestResult<T>(code, success, msg, data);
    }

    public static <T> RestResult<T> getRestResult(T t) {
        RestResult<T> RestResult = new RestResult<>(t);
        return RestResult;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }


}
