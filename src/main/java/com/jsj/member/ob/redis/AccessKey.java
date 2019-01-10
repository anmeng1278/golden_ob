package com.jsj.member.ob.redis;

public class AccessKey extends BasePrefix {

    private AccessKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static AccessKey withExpire(int expireSeconds) {
        return new AccessKey(expireSeconds, "access");
    }

    /**
     * 首页缓存Key
     */
    public static AccessKey pageIndex = new AccessKey(60, "pageIndex");

}
