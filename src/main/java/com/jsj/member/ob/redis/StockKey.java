package com.jsj.member.ob.redis;

public class StockKey extends BasePrefix {

    public StockKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static StockKey token = new StockKey(3600, "tk");

}
