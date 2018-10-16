package com.jsj.member.ob.constant;

import org.springframework.stereotype.Component;

@Component
public class Constant {

    /**
     * 管理员登录session
     */
    public static String LOGIN_SESSION_ADMIN_KEY = "login_admin";


    /**
     * 订单失败时间 (1800s)
     */
    public static int ORDER_EXPIRED_TIME = 1800;

    /**
     * 事务
     */
    public static final String DBTRANSACTIONAL = "txManager";

}
