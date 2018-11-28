package com.jsj.member.ob.redis;


public class ProductKey extends BasePrefix {

    public ProductKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

}
