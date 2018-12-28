package com.jsj.member.ob.redis;


import java.util.ArrayList;
import java.util.List;

public class ProductKey extends BasePrefix {

    public ProductKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public String INIT() {
        return String.format("%s:%s", this.getPrefix(), "INIT");
    }

    public String READYTIME() {
        return String.format("%s:%s", this.getPrefix(), "READYTIME");
    }

    public String RESULT() {
        return String.format("%s:%s", this.getPrefix(), "RESULT");
    }

    public List<String> TOTALS() {

        List<String> totals = new ArrayList<>();

        totals.add(this.INIT());
        totals.add(this.READYTIME());
        totals.add(this.RESULT());

        return totals;
    }
}
