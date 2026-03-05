package com.hss.exception;

import com.hss.pojo.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

//@RestControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(Exception.class) // 表示处理的所有异常
//
//    public Result handleException(Exception e){ // 捕获的异常对象
//        e.printStackTrace(); // 先将错误信息打印到控制台
//        // 获取错误信息e.getMessage() 但有些异常对象没有封装这个信息
//        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "操作失败");
//    }
//}
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result> handleValidationExceptions(MethodArgumentNotValidException ex) {
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
        return new ResponseEntity<>(Result.error(errorMessage), HttpStatus.BAD_REQUEST);
    }
}