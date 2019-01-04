package com.jsj.member.ob.utils;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class EncryptUtils {

    //算法名称
    public static final String KEY_ALGORITHM = "DES";
    //算法名称/加密模式/填充方式
    //DES共有四种工作模式-->>ECB：电子密码本模式、CBC：加密分组链接模式、CFB：加密反馈模式、OFB：输出反馈模式
    public static final String CIPHER_ALGORITHM = "DES/ECB/PKCS5Padding";

    //加密KEY
    public static final String SIGN_KEY = "*$(@^^^^E5F60987";

    /**
     * 生成密钥key对象
     *
     * @return 密钥对象
     * @throws NoSuchAlgorithmException
     * @throws Exception
     */
    private static SecretKey keyGenerator(String keyStr) throws Exception {
        byte input[] = HexString2Bytes(keyStr);
        DESKeySpec desKey = new DESKeySpec(input);
        //创建一个密匙工厂，然后用它把DESKeySpec转换成
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey securekey = keyFactory.generateSecret(desKey);
        return securekey;
    }

    private static int parse(char c) {
        if (c >= 'a') return (c - 'a' + 10) & 0x0f;
        if (c >= 'A') return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }

    // 从十六进制字符串到字节数组转换
    public static byte[] HexString2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int j = 0;
        for (int i = 0; i < b.length; i++) {
            char c0 = hexstr.charAt(j++);
            char c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    /**
     * 加密数据
     *
     * @param data 待加密数据
     * @return 加密后的数据
     */
    public static String encrypt(String data) throws Exception {
        return EncryptUtils.encrypt(data, SIGN_KEY);
    }

    /**
     * 加密数据
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的数据
     */
    public static String encrypt(String data, String key) throws Exception {


        Key deskey = keyGenerator(key);
        // 实例化Cipher对象，它用于完成实际的加密操作
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        SecureRandom random = new SecureRandom();
        // 初始化Cipher对象，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, deskey, random);
        byte[] results = cipher.doFinal(data.getBytes());
        // 该部分是为了与加解密在线测试网站（http://tripledes.online-domain-tools.com/）的十六进制结果进行核对
        //for (int i = 0; i < results.length; i++) {
        //    System.out.print(results[i] + " ");
        //}
        //System.out.println();
        // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
        return Base64.encodeBase64String(results);
    }

    /**
     * 解密数据
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return 解密后的数据
     */
    public static String decrypt(String data, String key) throws Exception {
        Key deskey = keyGenerator(key);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        //初始化Cipher对象，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        // 执行解密操作
        return new String(cipher.doFinal(Base64.decodeBase64(data)));
    }

    /**
     * 解密数据
     *
     * @param data 待解密数据
     * @return 解密后的数据
     */
    public static String decrypt(String data) throws Exception {
        return EncryptUtils.decrypt(data, SIGN_KEY);
    }

    /*
     * 加密用的Key 可以用26个字母和数字组成 此处使用AES-128-CBC加密模式，key需要为16位。
     */
    //公用_密钥
    public static final String SECURITY_COMMON_KEY = "$@china888zh99@$";
    //公用_偏移量
    public static final String SECURITY_COMMON_IV = "$@66love88naki@$";




    /**
     * 加密，使用指定的密码和偏移量
     *
     * @param data      原始数据
     * @param secretKey 密钥，长度16
     * @param vector    偏移量，长度16
     * @return 加密后的数据
     * @throws Exception 异常信息
     */
    public static String encrypt(String data, String secretKey, String vector) throws Exception {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = data.getBytes("utf-8");
            int plaintextLength = dataBytes.length;

            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keyspec = new SecretKeySpec(secretKey.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(vector.getBytes());

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
    public static String decrypt(String data, String secretKey, String vector) throws Exception {
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
     * 加密，使用通用的加密key和通用的偏移量
     *
     * @param data 被加密信息
     * @return 加密后信息
     * @throws Exception
     */
    public static String encrypt2(String data) throws Exception {
        return encrypt(data, SECURITY_COMMON_KEY, SECURITY_COMMON_IV);
    }

    /**
     * 解密，使用通用的加密key和通用的偏移量
     *
     * @param sSrc 被解密字符串
     * @return 解密结果
     * @throws Exception 异常信息
     */
    public static String decrypt2(String sSrc) throws Exception {
        try {
            return decrypt(sSrc, SECURITY_COMMON_KEY, SECURITY_COMMON_IV);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * 对字节进行偏移处理
     *
     * @param bytes 要处理的数组
     * @return 偏移后的结果
     */
    public static String encodeBytes(byte[] bytes) {
        StringBuffer strBuf = new StringBuffer();

        for (int i = 0; i < bytes.length; i++) {
            strBuf.append((char) (((bytes[i] >> 4) & 0xF) + ((int) 'a')));
            strBuf.append((char) (((bytes[i]) & 0xF) + ((int) 'a')));
        }

        return strBuf.toString();
    }


}
