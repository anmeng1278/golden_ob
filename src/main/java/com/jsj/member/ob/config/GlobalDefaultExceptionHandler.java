package com.jsj.member.ob.config;

import com.jsj.member.ob.dto.RestResponseBo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public RestResponseBo defaultExceptionHander(HttpServletRequest request, Exception e) throws Exception {

        return RestResponseBo.fail(e.getMessage());

    }
}
