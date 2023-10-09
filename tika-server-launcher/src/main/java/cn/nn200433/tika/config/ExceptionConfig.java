package cn.nn200433.tika.config;

import cn.hutool.core.util.StrUtil;
import cn.nn200433.tika.common.Result;
import cn.nn200433.tika.enums.ResponseEnum;
import cn.nn200433.tika.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 自定义错误处理
 *
 * @author Archer
 * @version 1.0
 * @date 2018/3/12 16:43
 */
@Slf4j
@RestControllerAdvice()
public class ExceptionConfig {

    /**
     * 捕捉Api异常
     *
     * @param ex 异常
     * @return {@link Object }
     */
    @ExceptionHandler(ApiException.class)
    public Object apiException(ApiException ex) {
        log.error(ex.getMessage(), ex);
        return Result.ofFail(ex.getResponseEnum(), ex.getMessage(), StrUtil.emptyToDefault(ex.getDebugMsg(), ex.getMessage()));
    }

    /**
     * 捕捉其他所有异常
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Object globalException(Exception ex) {
        log.error(ex.getMessage(), ex);
        // 全局异常捕捉
        return Result.ofDebugFail(ResponseEnum.SYSTEM_ERROR, ex.getMessage(), StrUtil.EMPTY);
    }

}