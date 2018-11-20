package com.jsj.member.ob.logic;

import org.junit.Test;

import java.util.stream.Stream;

public class StringUtilsTests {

    public static String camelCase(String str) {
        if (str.isEmpty())
            return str;
        return Stream.of(str.trim().split("\\s+")).map(e -> e.substring(0, 1).toLowerCase() + e.substring(1)).reduce("", (a, b) -> a+b);
    }


    @Test
    public void test(){
        String awkLogic = camelCase("RedEnvelopesLogic");
        System.out.println(awkLogic);

    }

}
