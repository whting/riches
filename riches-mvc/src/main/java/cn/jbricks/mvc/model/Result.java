package cn.jbricks.mvc.model;

import cn.jbricks.mvc.enums.ResultCodeEnum;

/**
 * 返回结果
 *
 * @Author: haoting.wang
 * @Date: Created in 下午3:00 2018/2/26
 */
public class Result<T> {

    private T model;

    private boolean state;

    private String message;

    private String code;

    private static final Result RS_FALSE_DEFAULT = new Result(false, "1000", "系统错误");
    private static final Result RS_SUCCESS_DEFAULT = new Result(true, "200", "默认的成功返回值");

    public Result(boolean state, String code, String message, T model) {
        this.state = state;
        this.code = code;
        this.message = message;
        this.model = model;
    }

    public Result(boolean state, String code, String message) {
        this.state = state;
        this.code = code;
        this.message = message;
    }

    public static Result valueOfSuccess() {
        return RS_SUCCESS_DEFAULT;
    }

    public static <M> Result valueOfSuccess(M model) {
        return new Result(RS_SUCCESS_DEFAULT.state, RS_SUCCESS_DEFAULT.code, RS_SUCCESS_DEFAULT.message, model);
    }

    public static <M> Result valueOfSuccess(String code, String message, M model) {
        return new Result(RS_FALSE_DEFAULT.state, code, message, model);
    }


    public static Result valueOfError() {
        return RS_FALSE_DEFAULT;
    }

    public static Result valueOfError(String code, String message) {
        return new Result(RS_FALSE_DEFAULT.state, code, message);
    }

    public static Result valueOfError(String message) {
        return new Result(RS_FALSE_DEFAULT.state, RS_FALSE_DEFAULT.code, message);
    }

    public static Result valueOfError(ResultCodeEnum codeEnum) {
        return new Result(RS_FALSE_DEFAULT.state, codeEnum.getCode(), codeEnum.getDesc());
    }

    //判断返回是否成功
    public boolean isSuccess() {
        return state;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Result) {
            return ((Result) o).getCode() == this.getCode();
        } else {
            throw new RuntimeException("[Result-equals], o instanceof Result is false");
        }
    }

    public int hashCode() {
        String code = String.valueOf(this.getCode());
        return code.hashCode();
    }

    @Override
    public String toString() {
        return "[code=" + this.getCode() +
                ";message=" + this.getMessage() + "]";
    }

    public T getModel() {
        return model;
    }

    public void setModel(T model) {
        this.model = model;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
