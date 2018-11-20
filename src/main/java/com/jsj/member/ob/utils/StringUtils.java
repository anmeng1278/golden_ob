package com.jsj.member.ob.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class StringUtils {

    public static String UUID32() {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        return uuid;
    }

    public static String Token() {
        return StringUtils.UUID32().substring(0, 24);
    }

    /**
     * 判断是否正确手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {

        if (org.apache.commons.lang3.StringUtils.isBlank(mobile)) {
            return false;
        }

        String regex = "^1\\d{10}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        boolean isMatch = m.matches();
        return isMatch;

    }

    /**
     * 判断是否正确邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {

        if (org.apache.commons.lang3.StringUtils.isBlank(email)) {
            return false;
        }

        String regex = "^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*\\.[a-zA-Z0-9]{2,6}$";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        boolean isMatch = m.matches();
        return isMatch;

    }


    /**
     * 解析支付金额
     *
     * @param regEx
     * @param s
     * @param grounpName
     * @return
     */
    public static Double RegexPayAmount(String regEx, String s, String grounpName) {

        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(s);
        if (mat.find()) {
            String value = mat.group(grounpName);
            return Double.valueOf(value);
        }
        return -1d;

    }

    public static String streamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine()
         * method. We iterate until the BufferedReader return null which means
         * there's no more data to read. Each line will appended to a StringBuilder
         * and returned as String.
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    //判断整数（int）
    public static boolean isInteger(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    //判断浮点数（double和float）
    public static boolean isDouble(String str) {
        if (null == str || "".equals(str)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * base64编码
     *
     * @param input
     * @return
     * @throws Exception
     */
    public static String encodeBase64(byte[] input) throws Exception {
        Class clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
        Method mainMethod = clazz.getMethod("encode", byte[].class);
        mainMethod.setAccessible(true);
        Object retObj = mainMethod.invoke(null, new Object[]{input});
        return (String) retObj;
    }


    /**
     * base64解码
     *
     * @param input
     * @return
     * @throws Exception
     */
    public static String decodeBase64(String input) throws Exception {
        Class clazz = Class.forName("com.sun.org.apache.xerces.internal.impl.dv.util.Base64");
        Method mainMethod = clazz.getMethod("decode", String.class);
        mainMethod.setAccessible(true);
        Object retObj = mainMethod.invoke(null, input);
        return new String((byte[]) retObj);
    }

    /**
     * 驼峰命名
     * @param str
     * @return
     */
    public static String camelCase(String str) {
        if (str.isEmpty())
            return str;
        return Stream.of(str.trim().split("\\s+")).map(e -> e.substring(0, 1).toLowerCase() + e.substring(1)).reduce("", (a, b) -> a+b);
    }

}
