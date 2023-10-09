package cn.nn200433.tika.exception;

import cn.nn200433.tika.enums.BaseEnum;
import lombok.Data;

/**
 * Api异常
 *
 * @author song_jx
 * @date 2023-10-09 04:22:58
 */
@Data
public class ApiException extends RuntimeException {

    /** 返回码 */
    private int      code;
    /** 消息 */
    private String   msg;
    /** 调试消息 */
    private String   debugMsg;
    /** 响应枚举 */
    private BaseEnum responseEnum;

    public ApiException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg  = msg;
    }

    public ApiException(BaseEnum responseEnum) {
        super(responseEnum.getMsg());
        this.responseEnum = responseEnum;
        this.code         = responseEnum.getRet();
        this.msg          = responseEnum.getMsg();
    }

    public ApiException(BaseEnum responseEnum, String msg) {
        super(msg);
        this.responseEnum = responseEnum;
        this.code         = responseEnum.getRet();
        this.msg          = msg;
    }

    public ApiException(BaseEnum responseEnum, String msg, String debugMsg) {
        super(msg);
        this.debugMsg     = debugMsg;
        this.responseEnum = responseEnum;
        this.code         = responseEnum.getRet();
        this.msg          = msg;
    }

    public ApiException(int code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
        this.msg  = msg;
    }

}
