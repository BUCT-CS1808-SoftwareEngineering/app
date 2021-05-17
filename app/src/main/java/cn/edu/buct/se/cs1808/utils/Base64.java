package cn.edu.buct.se.cs1808.utils;

import java.io.UnsupportedEncodingException;

public class Base64 {
    /**
     * 字符Base64加密
     * @param str 原文字符串
     * @return 编码后结果
     */
    public static String encodeToString(String str){
        try {
            return android.util.Base64.encodeToString(str.getBytes("UTF-8"), android.util.Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 字符Base64解密
     * @param str 待解密字符串
     * @return 解密结果
     */
    public static String decodeToString(String str){
        try {
            return new String(android.util.Base64.decode(str.getBytes("UTF-8"), android.util.Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
