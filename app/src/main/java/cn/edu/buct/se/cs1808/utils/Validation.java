package cn.edu.buct.se.cs1808.utils;

import android.annotation.SuppressLint;

public class Validation {

    /**
     * 验证text的长度是否在某个范围内
     * @param text 需要验证的字段
     * @param min 最小长度
     * @param max 最大长度
     * @param name 字段名称
     * @return 验证通过返回null，验证失败返回错误消息
     */
    public static String lengthBetween(String text, int min, int max, String name) {
        if (text == null) {
            return  String.format("%s 不能为空", name);
        }
        if (text.length() < min || text.length() > max) {
            return String.format("%s长度必须在%d和%d值之间", name, min, max);
        }
        return null;
    }
}
