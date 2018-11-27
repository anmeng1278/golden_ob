package com.jsj.member.ob.enums;

import java.util.HashMap;

public enum SingletonMap {

    INSTANCE;

    private static HashMap<String, String> map = new HashMap<>();

    public boolean Put(String key, String value) {
        if (map.containsKey(key)) {
            return false;
        }
        map.put(key, value);
        return true;
    }

    public void Remove(String key) {
        if (map.containsKey(key)) {
            map.remove(key);
        }
    }
}
