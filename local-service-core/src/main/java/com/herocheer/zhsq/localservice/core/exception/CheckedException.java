package com.herocheer.zhsq.localservice.core.exception;

import com.herocheer.zhsq.localservice.core.util.MessageUtil;

import java.util.Arrays;

public class CheckedException extends RuntimeException {


    /** 异常码 */
    protected String errorCode;

    /**
     * @param errorCode 异常码
     */
    public CheckedException(String errorCode) {
        super(MessageUtil.getCodeMessage(errorCode));
        this.errorCode = errorCode;
    }


    /**
     * @param errorCode 异常码
     * @param values 对应的占位符 value值
     */
    public CheckedException(String errorCode, String... values) {
        super(MessageUtil.getCodeMessage(errorCode, values));
        this.errorCode = errorCode;
    }

    /**
     * @param errorCode 异常码
     * @param values 对应的占位符 value值
     */
    public CheckedException(String errorCode, Integer... values) {
        super(MessageUtil.getCodeMessage(errorCode, (String[]) Arrays.stream(values).map(x -> String.valueOf(x)).toArray()));
        this.errorCode = errorCode;
    }

    /**
     * @param errorCode 异常码
     * @param cause 异常链
     */
    public CheckedException(String errorCode, Throwable cause) {
        super(MessageUtil.getCodeMessage(errorCode), cause);
        this.errorCode = errorCode;
    }

    /**
     * @param errorCode 异常码
     * @param cause 异常链
     * @param values 对应的占位符 value值
     */
    public CheckedException(String errorCode, Throwable cause, String[] values) {
        super(MessageUtil.getCodeMessage(errorCode, values), cause);
        this.errorCode = errorCode;
    }

    /**
     * 获取异常码
     * @return 异常码
     */
    public String getErrorCode() {
        return this.errorCode;
    }

    /**
     * 抛出 CheckedException 异常
     * @param errorCode 异常码
     */
    public static void throwException(String errorCode) {
        throw new CheckedException(errorCode);
    }

    /**
     * 抛出 CheckedException 异常
     * @param errorCode 异常码
     * @param values 对应的占位符 value值
     */
    public static void throwException(String errorCode, String... values) {
        throw new CheckedException(errorCode, values);
    }

    /**
     * 抛出 CheckedException 异常
     * @param errorCode 异常码
     * @param cause 异常链
     */
    public static void throwException(String errorCode, Throwable cause) {
        throw new CheckedException(errorCode, cause);
    }

    /**
     * 抛出 CheckedException 异常
     * @param errorCode 异常码
     * @param cause 异常链
     * @param values 对应的占位符 value值
     */
    public static void throwException(String errorCode, Throwable cause, String... values) {
        throw new CheckedException(errorCode, cause, values);
    }

    /**
     * 抛出 CheckedException 异常
     * @param expression 表达式为true 时 抛出CheckedException, false 不抛出
     * @param errorCode 异常码
     */
    public static void throwException(boolean expression, String errorCode) {
        if (expression)
            throw new CheckedException(errorCode);
    }

    /**
     * 抛出 CheckedException 异常
     * @param expression 表达式为true 时 抛出CheckedException, false 不抛出
     * @param errorCode 异常码
     * @param values 对应的占位符 value值
     */
    public static void throwException(boolean expression, String errorCode, String... values) {
        if (expression)
            throw new CheckedException(errorCode, values);
    }

    /**
     * 抛出 CheckedException 异常
     * @param expression 表达式为true 时 抛出CheckedException, false 不抛出
     * @param errorCode 异常码
     * @param cause 异常链
     */
    public static void throwException(boolean expression, String errorCode, Throwable cause) {
        if (expression)
            throw new CheckedException(errorCode, cause);
    }

    /**
     * 抛出 CheckedException 异常
     * @param expression 表达式为true 时 抛出CheckedException, false 不抛出
     * @param errorCode 异常码
     * @param cause 异常链
     * @param values 对应的占位符 value值
     */
    public static void throwException(boolean expression, String errorCode, Throwable cause, String... values) {
        if (expression)
            throw new CheckedException(errorCode, cause, values);
    }
}
