package cn.nn200433.tika.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 响应枚举
 *
 * @author song_jx
 * @date 2023-10-09 04:20:58
 */
@Getter
public enum ResponseEnum implements BaseEnum {

    SUCCESS(200, "成功"),
    NO_AUTHORIZED(401, "没有该权限,请联系管理员"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "无法访问"),
    ERROR_METHOD(405, "访问方式错误"),
    ERROR(500, "系统维护中，请稍后再试"),
    TIMEOUT(504, "请求已超时"),
    REQUEST_PARAM_MISSING(601, "请求参数缺失"),
    REQUEST_PARAM_ERROR(602, "参数错误"),
    REQUEST_PARAM_INVALID(603, "请求参数不符合规范"),
    UPLOAD_ERROR(9998, "文件上传出错"),
    SYSTEM_ERROR(9999, "系统执行出错");

    private static final Map<Integer, ResponseEnum> MAP = new HashMap<Integer, ResponseEnum>();

    static {
        for (ResponseEnum item : ResponseEnum.values()) {
            MAP.put(item.ret, item);
        }
    }

    private int    ret;
    private String msg;

    ResponseEnum(int ret, String msg) {
        this.ret = ret;
        this.msg = msg;
    }

    public static ResponseEnum getByCode(int ret) {
        return MAP.get(ret);
    }

}