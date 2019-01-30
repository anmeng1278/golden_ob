package com.jsj.member.ob.constant;

import org.springframework.stereotype.Component;

@Component
public class Constant {

    /**
     * 微信小程序的session_key键
     */
    public static final String wxapp_session_key = "WXAPP_SESSION_KEY";

    /**
     * 管理员登录session
     */
    public static String LOGIN_SESSION_ADMIN_KEY = "login_admin";


    /**
     * 订单失效时间 (1800s)
     */
    public static int ORDER_EXPIRED_TIME = 1800;

    /**
     * 事务
     */
    public static final String DBTRANSACTIONAL = "txManager";

    /**
     * 领取失效时间 (24H)
     */
    public static int GIFT_EXPIRED_TIME = 60 * 60 * 24;

}
