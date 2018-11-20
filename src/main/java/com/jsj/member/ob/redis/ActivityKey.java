package com.jsj.member.ob.redis;


public class ActivityKey extends BasePrefix {

    private ActivityKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static ActivityKey SecKill = new ActivityKey(-1, "");
}
