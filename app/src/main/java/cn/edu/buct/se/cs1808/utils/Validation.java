package cn.edu.buct.se.cs1808.utils;


import java.util.Objects;

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

    /**
     * 验证两个给定的字段是否相等
     * @param a 字段A
     * @param b 字段B
     * @param nameA 字段A的名字
     * @param nameB 字段B的名字
     * @return 错误消息或者NULL
     */
    public static String equals(String a, String b, String nameA, String nameB) {
        if (!Objects.equals(a, b)) {
            return String.format("%s与%s必须相等", nameA, nameB);
        }
        return null;
    }
}
