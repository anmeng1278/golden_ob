package com.jsj.member.ob.config;

import com.jsj.member.ob.dto.RestResponseBo;
import com.jsj.member.ob.dto.api.Response;
import com.jsj.member.ob.exception.TipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {


    private final Logger logger = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object defaultExceptionHander(HttpServletRequest request, Exception e) throws Exception {

        String url = request.getRequestURI().toString().toLowerCase();

        String errorCode = "0001";
        if (url.indexOf("mini") > -1) {

            if (e instanceof TipException) {
                TipException ex = (TipException) e;
                if (ex.getCode() > 0) {
                    errorCode = ex.getCode() + "";
                }
            }

            return Response.fail(e.getMessage(), errorCode);
        }

        return RestResponseBo.fail(e.getMessage());

    }
}
