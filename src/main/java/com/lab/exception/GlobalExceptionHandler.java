package com.lab.exception;

import com.lab.pojo.Result;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 获取验证错误的结果
        BindingResult result = ex.getBindingResult();

        // 提取所有字段错误的默认消息并汇总成一个字符串
        String errorMessage = result.getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        // 如果没有找到任何错误信息，则提供一个默认的错误消息
        if (!StringUtils.hasLength(errorMessage)) {
            errorMessage = "操作失败";
        }

        // 返回更简洁的错误信息给客户端
        return Result.error(errorMessage);
    }

    /**
     * 处理业务异常（RuntimeException）
     * 捕获所有 RuntimeException，返回友好的错误信息
     */
    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException ex) {
        // 打印异常堆栈到控制台（用于调试）
        ex.printStackTrace();
        
        // 返回友好的错误信息
        String errorMessage = StringUtils.hasLength(ex.getMessage()) ? ex.getMessage() : "操作失败";
        return Result.error(errorMessage);
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception ex) {
        // 打印异常堆栈到控制台（用于调试）
        ex.printStackTrace();
        
        // 返回友好的错误信息
        String errorMessage = StringUtils.hasLength(ex.getMessage()) ? ex.getMessage() : "操作失败";
        return Result.error(errorMessage);
    }
}