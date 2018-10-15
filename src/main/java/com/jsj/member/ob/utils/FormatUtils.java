package com.jsj.member.ob.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FormatUtils {
    /**
     * 格式化
     *
     * @param jsonStr
     * @return
     * @author lizhgb
     * @Date 2015-10-14 下午1:17:35
     * @Modified 2017-04-28 下午8:55:35
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '"':
                    if (last != '\\') {
                        isInQuotationMarks = !isInQuotationMarks;
                    }
                    sb.append(current);
                    break;
                case '{':
                case '[':
                    sb.append(current);
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '}':
                case ']':
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\' && !isInQuotationMarks) {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     * @author lizhgb
     * @Date 2015-10-14 上午10:38:04
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
    }


    public static String GetAppSize(Integer v) {

        if (v == null) {
            return "0 M";
        }

        double appSize = Double.parseDouble(v + "") / (1024d * 1024d);
        String appSizeValue = new BigDecimal(appSize).setScale(2, BigDecimal.ROUND_HALF_UP).toString() + " M";

        return appSizeValue;
    }

    public static String GetAppVersion(Integer v) {

        if (v == null || v <= 0) {
            return "";
        }
        String vl = v + "";
        char[] chars = vl.toCharArray();

        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(String.format("%s.", c + ""));
        }
        return sb.substring(0, sb.length() - 1);

    }

}