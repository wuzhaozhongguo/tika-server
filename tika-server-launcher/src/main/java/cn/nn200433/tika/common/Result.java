package cn.nn200433.tika.common;

import cn.nn200433.tika.enums.BaseEnum;
import cn.nn200433.tika.enums.ResponseEnum;
import lombok.Data;

/**
 * 后果 通用返回实体类
 *
 * @author song_jx
 * @date 2023-10-09 04:25:15
 */
@Data
public class Result<R> {

    /** 是否成功 */
    private boolean success;
    /** 返回码 */
    private int     code;
    /** 提示信息 */
    private String  msg;
    /** 后台错误信息 */
    private String  debugMsg;
    /** 数据 */
    private R       data;
    /** 请求id */
    private String  requestId;

    public static <R> Result<R> ofSuccess(ResponseEnum responseEnum, R data) {
        return new Result<R>()
                .setSuccess(true)
                .setCode(responseEnum.getRet())
                .setMsg("success")
                .setData(data);
    }

    public static <R> Result<R> ofSuccess(R data) {
        return new Result<R>()
                .setSuccess(true)
                .setCode(ResponseEnum.SUCCESS.getRet())
                .setMsg("success")
                .setData(data);
    }

    public static <R> Result<R> ofSuccessMsg(String msg) {
        return new Result<R>()
                .setSuccess(true)
                .setCode(ResponseEnum.SUCCESS.getRet())
                .setMsg(msg);
    }

    public static <R> Result<R> ofSuccess(String msg, R data) {
        return new Result<R>()
                .setSuccess(true)
                .setCode(ResponseEnum.SUCCESS.getRet())
                .setMsg(msg)
                .setData(data);
    }

    public static <R> Result<R> ofSampleFail(BaseEnum responseEnum, String requestId) {
        return new Result<R>()
                .setDebugMsg(responseEnum.getMsg())
                .setSuccess(false)
                .setCode(responseEnum.getRet())
                .setMsg(responseEnum.getMsg())
                .setRequestId(requestId);
    }

    public static <R> Result<R> ofFail(BaseEnum responseEnum) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(responseEnum.getRet())
                .setMsg(responseEnum.getMsg());
    }

    public static <R> Result<R> ofFail(BaseEnum responseEnum, String msg) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(responseEnum.getRet())
                .setMsg(msg)
                .setDebugMsg(msg);
    }

    public static <R> Result<R> ofFail(BaseEnum responseEnum, String msg, String requestId) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(responseEnum.getRet())
                .setMsg(msg)
                .setDebugMsg(msg)
                .setRequestId(requestId);
    }

    public static <R> Result<R> ofDebugFail(BaseEnum responseEnum, String debugMsg, String requestId) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(responseEnum.getRet())
                .setMsg(responseEnum.getMsg())
                .setDebugMsg(debugMsg)
                .setRequestId(requestId);
    }

    public static <R> Result<R> ofFail(BaseEnum responseEnum, String msg, String debugMsg, String requestId) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(responseEnum.getRet())
                .setMsg(msg)
                .setDebugMsg(debugMsg)
                .setRequestId(requestId);
    }

    public static <R> Result<R> ofFail(int code, String msg) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(code)
                .setMsg(msg)
                .setDebugMsg(msg);
    }

    public static <R> Result<R> ofFail(int code, String msg, String requestId) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(code)
                .setMsg(msg)
                .setDebugMsg(msg)
                .setRequestId(requestId);
    }


    public static <R> Result<R> ofFail(int code, String msg, String debugMsg, String requestId) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(code)
                .setMsg(msg)
                .setDebugMsg(debugMsg)
                .setRequestId(requestId);
    }

    public static <R> Result<R> ofThrowable(int code, Throwable throwable) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(code)
                .setMsg(throwable.getClass().getName() + ", " + throwable.getMessage());
    }


    public Result<R> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public Result<R> setCode(int code) {
        this.code = code;
        return this;
    }

    public Result<R> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Result<R> setData(R data) {
        this.data = data;
        return this;
    }

    public Result<R> setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public Result<R> setDebugMsg(String debugMsg) {
        this.debugMsg = debugMsg;
        return this;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

}