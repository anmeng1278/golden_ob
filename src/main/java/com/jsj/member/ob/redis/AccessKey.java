package com.jsj.member.ob.redis;

public class AccessKey extends BasePrefix {

    public AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }

    /**
     * 首页缓存Key
     */
    public static AccessKey pageIndex = new AccessKey(7200, "pageIndex");

    /**
     * 兑换页面缓存
     */
    public static AccessKey pageExchange = new AccessKey(7200, "pageExchange");


}
