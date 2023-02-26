package com.light.basegert.advice;

import cn.dev33.satoken.exception.NotLoginException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotLoginExceptionAdvice {

    @ExceptionHandler(NotLoginException.class)
    public String handlerNotLoginException(cn.dev33.satoken.exception.NotLoginException nle)
            throws Exception {

        // 打印堆栈，以供调试
        nle.printStackTrace();


        // 返回给前端
        return "redirect:/login";
    }
}
