package com.jsj.member.ob.utils;

import org.junit.Test;

public class EncryptUtilsTest {

    @Test
    public void encrypt() throws Exception {
        String encrypt = EncryptUtils.encrypt("10020");
        System.out.println(encrypt);
        //2rpaS3MKnYw=

    }

    @Test
    public void decrypt() throws Exception {
        String decrypt = EncryptUtils.decrypt("2rpaS3MKnYw=");
        System.out.println(decrypt);
    }
}