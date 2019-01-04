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

        String decrypt1 = EncryptUtils.decrypt2("zaE8Ra+oECo69Uh7kbbPaQ==");

        System.out.println(decrypt1);
        System.out.println("15810212304");
    }

    @Test
    public void rep(){

        String url = "http://localhost/ot/product?activityId=22&productSpecId=12&activityType=40";
        url = url.replaceAll("http", "https");

        System.out.println(url);
    }
}