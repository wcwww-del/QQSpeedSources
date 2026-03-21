package com.qqspeed.common.exception;

import com.qqspeed.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 业务异常
    @ExceptionHandler(BusinessException.class)
    public Result<?> businessExceptionHandler(BusinessException e) {
        log.error("业务异常：{}", e.getMessage(), e);
        return Result.error(e.getCode(), e.getMessage());
    }

    // 系统异常
    @ExceptionHandler(Exception.class)
    public Result<?> systemExceptionHandler(Exception e) {
        log.error("系统异常：{}", e.getMessage(), e);
        return Result.error("服务器内部错误，请联系管理员");
    }
}