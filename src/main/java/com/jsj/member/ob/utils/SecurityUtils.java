package com.jsj.member.ob.utils;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.AlgorithmParameters;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

//import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * @author ljy
 * @create 2019-01-05
 * @desc 加解密
 */
public class SecurityUtils {

    /**
     * 密钥
     */
    public final static String AES_KEY = "123qweasdzxc!@#$";
    public final static String AES_IV = "!@#$qweasdzxc123";
    public final static String DEF_MD5_KEY = "qweasdzxc123!@#$";
    public final static String SIGN_TYPE_MD5 = "MD5";
    public final static String SIGN_TYPE_SRA = "SRA";
    //算法名称
    public static final String KEY_ALGORITHM = "DES";
    //算法名称/加密模式/填充方式
    //DES共有四种工作模式-->>ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
    public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

    /**
     * 加密
     * @param encryptStr
     * @param aesKey
     * @return
     */
    public static String encodeAES(String encryptStr,String aesKey){
        String result=null;
        if(StringUtils.isBlank(encryptStr)){
            result="";
        }else {
            if(StringUtils.isBlank(aesKey)){
                result= SecurityUtils.encodeAES(encryptStr,SecurityUtils.AES_KEY,SecurityUtils.AES_IV);
            }else{
                result=SecurityUtils.encodeAES(encryptStr,aesKey,SecurityUtils.AES_IV);
            }
        }
        return result;
    }

    /**
     * 加密
     * @param text 原始数据
     * @param key 密钥，长度16
     * @param iv 偏移量，长度16
     * @return 加密后的数据
     */
    public static String encodeAES(String text,String key,String iv){
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = text.getBytes("utf-8");
            int plaintextLength = dataBytes.length;

            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);

            String aesStr = new String(Base64Utils.encode(encrypted, Base64Utils.DEFAULT)).replace("\n", "");
            return aesStr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密，使用指定的密码和偏移量
     *
     * @param data      被解密字符串
     * @param secretKey 密钥，长度16
     * @param vector    偏移量，长度16
     * @return 解密结果
     * @throws Exception 异常信息
     */
    public static String decryptAES(String data, String secretKey, String vector) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            SecretKeySpec keyspec = new SecretKeySpec(secretKey.getBytes("utf-8"), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(vector.getBytes("utf-8"));

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] encrypted1 = Base64Utils.decode(data.getBytes("utf-8"), Base64Utils.DEFAULT);// 先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString.trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 微信用户信息解密
     * @param key
     * @param iv
     * @param encrypData
     * @return
     * @throws Exception
     */
    public static String decrypt(byte[] key,byte[] iv,byte[] encrypData) throws Exception {
        // 如果密钥不足16位，那么就补足.  这个if 中的内容很
        int base = 16;
        if (key.length % base != 0) {
            int groups = key.length / base + (key.length % base != 0 ? 1 : 0);
            byte[] temp = new byte[groups * base];
            Arrays.fill(temp, (byte) 0);
            System.arraycopy(key, 0, temp, 0, key.length);
            key = temp;
        }
        // 初始化
        //Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding", "BC");
        AlgorithmParameters ivSpec = AlgorithmParameters.getInstance("AES");
        ivSpec.init(new IvParameterSpec(iv));
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);// 初始化
        byte[] resultByte = cipher.doFinal(encrypData);
        return new String(resultByte, "UTF-8");
    }

    public static void main(String[] args) throws Exception {
        byte[] encrypData = Base64.decodeBase64("mgxuts7uEbdnBclp0Qim0TS3MGoaSyE09MqTvbeG9Z1PjxsDwjVH0FxG1Q==");
        byte[] ivData = Base64.decodeBase64("SG386etdA3sOXHxqfnw==");
        byte[] sessionKey = Base64.decodeBase64("Fn6r4IOiZJBXn4hQ0w==");
        System.out.println(decrypt(sessionKey,ivData,encrypData));
    }

    public static String decrypt2(byte[] key, byte[] iv, byte[] encData) throws Exception {
        AlgorithmParameterSpec ivSpec = new IvParameterSpec(iv);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        //解析解密后的字符串
        return new String(cipher.doFinal(encData),"UTF-8");
    }
}
