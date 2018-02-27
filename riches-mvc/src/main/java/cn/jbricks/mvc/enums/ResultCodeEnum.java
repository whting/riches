package cn.jbricks.mvc.enums;

/**
 * file: ReasonCode User: toruneko Date: 15/7/14 14:01 Description: 状态码枚举
 */
public enum ResultCodeEnum {

                            AUTH_ERROR("000", "认证失败"), // 因系统认证引起的其他未知错误
                            AUTH_FAILED("001", "用户认证失败"), // 合作方标识和合作方密钥认证失败
                            AUTH_EXPIRED("002", "授权过期"), // ???
                            PASSWORD_AUTH_FAILED("003", "用户密码认证失败"), // 登录用户名和密码认证失败
                            AUTH_UN_ACTIVATION("004", "用户账号未激活"), // 用户账号未激活
                            USERNAME_EXIST("005", "用户账号已存在"), // 用户账号已存在
                            USERNAME_EMPTY("006", "用户账号为空"), // 用户账号为空
                            PASSWARD_EMPTY("007", "密码为空"), // 密码为空

                            PARAM_ERROR("100", "非法参数"), // 因参数引起的其他未知错误
                            PARAM_NULL_ERROR("101", "参数不能为空"), // 参数不能为空
                            PARAM_DATA_TYPE_ERROR("102", "参数类型不正确"), // 例如：Integer类型传入了String类型值
                            PARAM_OVER_MAX_LEN("103", "参数超过最大长度"), // 个别参数有长度限制
                            PARAM_FORMAT_ERROR("104", "参数格式不正确"), // 例如：字符串时间参数格式不正确
                            QUERY_INTERVAL_INVALID("105", "参数范围不正确"), // 例如：起始时间大于截止时间
                            PARAM_DATA_NOT_EXIST_ERROR("106", "枚举值不存在"), // 例如：字符串枚举类型传入了一个不存在的枚举值

                            SUCCESS("200", "请求已完成"), //
                            NO_CONTENT("204", "无响应"), // 已接收请求，但不存在要回送的信息（例如：报告未生成）
                            PARTIAL_CONTENT("206", "部分信息"), // 处理（返回）信息只是一部分

                            UNAVAILABLE("300", "服务不可用"), // 因服务不可用引起的未知错误
                            NOT_BUY_SERVICE("301", "服务未购买"), // 同401：未授权
                            NOT_ALLOWED("302", "服务已被禁用"), // 同403：禁止访问
                            FLOW_POOR("303", "流量不足"), // 同402：需要付款
                            OUT_OF_SERVICE_DATE("304", "服务时间过期"), // 同410：授权已过期

                            BAD_REQUIRED("400", "找不到"), // 因访问资源找不到引起的其他未知错误
                            UNAUTHORIZED("401", "未授权"), // 未取得访问资源的授权
                            PAYMENT_REQUIRED("402", "需要付款"), // 计费流量已用尽，需要付款后再试
                            FORBIDDEN("403", "禁止访问"), // 即使获取授权也不能访问
                            NOT_FOUND("404", "找不到资源"), // 没有对应的策略配置，没有对应的资源
                            METHOD_NOT_ALLOWED("405", "不允许的请求方法"), // 服务器不支持的请求方法，例如不支持PUT操作
                            GONE("410", "授权已过期"), // 取得的授权已经过期
                            UNSUPPORTED_MEDIA_TYPE("415", "不支持的媒体类型"), // Content-Type不支持

                            INTERNAL_ERROR("500", "内部执行错误"), // 因内部执行错误引起的其他未知错误
                            NOT_IMPLEMENTED("501", "未执行"), // 服务器拒绝执行请求资源
                            BAD_GATEWAY("502", "错误网关"), // NGX预留状态码,服务器接收到来自上游服务器的无效响应
                            SERVICE_UNAVAILABLE("503", "无法获得服务"), // NGX预留状态码,由于临时过载或维护，服务器无法处理请求

                            TIMEOUT("600", "超时"), // 因超时引起的其他未知错误
                            POLICY_EXECUTE_TIMEOUT("601", "异步执行"), // 策略执行超时转成异步执行

                            OTHERS("700", "业务逻辑错误");

    private String code;
    private String desc;

    ResultCodeEnum(String code, String desc){
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public String toString() {
        return code + ":" + desc;
    }
}
