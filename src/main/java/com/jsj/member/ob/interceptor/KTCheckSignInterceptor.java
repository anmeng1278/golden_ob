package com.jsj.member.ob.interceptor;


import com.jsj.member.ob.exception.TipException;
import com.jsj.member.ob.utils.SpringContextUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 验签拦截器
 */
@Service
public class KTCheckSignInterceptor extends HandlerInterceptorAdapter {

    /**
     * 验签的盐
     */
    private static final String SALT = "k89DmlxuTqpNXLg7GYnOdIUaA1jMcwtb";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //测试环境不验签
        String activeProfile = SpringContextUtils.getActiveProfile();
        if ("dev".equals(activeProfile)) {
            return true;
        }

        if (handler instanceof HandlerMethod) {

            HandlerMethod hm = (HandlerMethod) handler;
            KTCheckSign ktCheckSign = hm.getMethodAnnotation(KTCheckSign.class);
            if (ktCheckSign == null || !ktCheckSign.checkSign()) {
                return true;
            }

            // 校验签名
            MultiReadHttpServletRequest multiReadHttpServletRequest = new MultiReadHttpServletRequest(request);
            String authorization = multiReadHttpServletRequest.getHeader("authorization");
            if (StringUtils.isBlank(authorization)|| !verifyAuthorization(multiReadHttpServletRequest, authorization)) {
                throw new TipException("验签失败", 401);
            }

        }
        return super.preHandle(request, response, handler);

    }

    /**
     * 验证authorization
     *
     * @param request       ServletRequest 请求体
     * @param authorization 输入的授权码
     * @return 授权是否通过
     */
    private boolean verifyAuthorization(MultiReadHttpServletRequest request, String authorization) {
        StringBuilder jsonString = new StringBuilder();

        try {
            BufferedReader br = request.getReader();
            String str;
            while ((str = br.readLine()) != null) {
                jsonString.append(str
//                        .replaceAll("\n", "")
//                        .replaceAll("\r", "")
//                        .replaceAll(" ", "")
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        // 授权验证
        return authorization.equals(DigestUtils.md5Hex(DigestUtils.md5Hex(jsonString + SALT)));
    }
}
