package com.light.basegert.advice;

import cn.dev33.satoken.util.SaResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@RestControllerAdvice
public class SqlExceptionHandler {
    @ExceptionHandler(SQLException.class)
    public SaResult handlerNotLoginException(SQLException e)
            throws Exception {

        // 打印堆栈，以供调试
        e.printStackTrace();


        // 返回给前端
        return new SaResult(SaResult.CODE_ERROR,"执行sql语句发生错误",e.getMessage());
    }

}
