package com.jsj.member.ob.redis;


public class ActivityKey extends BasePrefix {

    public ActivityKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

}
