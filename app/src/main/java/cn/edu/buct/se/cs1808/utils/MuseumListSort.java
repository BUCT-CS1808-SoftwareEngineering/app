package cn.edu.buct.se.cs1808.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MuseumListSort {

    /**
     * 根据关键词权重对JSONArray排序，通过比较关键词与array中的每一项的muse_Name排序
     * @param searchWord 关键词
     * @param museums 博物馆信息列表
     * @return 排序后的博物馆信息列表，排序保证返回的JSONArray长度与传入的长度一致
     */
    public static JSONArray sort(String searchWord, JSONArray museums) {
        Log.i("NeedSort", String.valueOf(museums.length()));
        List<String> keys = splitWord(searchWord, 2);
        List<Boolean> flags = new ArrayList<>(museums.length());
        for (int i = 0; i < museums.length(); i ++) {
            flags.add(false);
        }
        JSONArray res = new JSONArray();
        // 从关键词第一个进行匹配，先匹配到的说明重合度越大，优先级越高
        for (String key : keys) {
            for (int i = 0; i < museums.length(); i ++) {
                if (flags.get(i)) {
                    continue;
                }
                try {
                    JSONObject museum = museums.getJSONObject(i);
                    String name = museum.getString("muse_Name");
                    if (name.contains(key)) {
                        flags.set(i, true);
                        res.put(museum);
                    }
                }
                catch (JSONException ignore) {}
            }
        }
        for (int i = 0; i < museums.length(); i ++) {
            if (flags.get(i)) {
                continue;
            }
            try {
                JSONObject museum = museums.getJSONObject(i);
                res.put(museum);
            }
            catch (JSONException ignore) {}
        }
        Log.i("SortRes", String.valueOf(res.length()));
        return res;
    }

    /**
     * 分割关键词，若关键词的长度为n,分别取长度为n,n-1,n-2,……,minLength 长度的子串作为关键词
     * @param word 关键词
     * @param minLength 最小子串长度
     * @return 关键词列表
     */
    private static List<String> splitWord(String word, int minLength) {
        List<String> res = new ArrayList<>();
        int n = word.length();
        for (int i = n; i >= minLength; i --) {
            for (int j = 0; j < n - i + 1; j ++) {
                res.add(word.substring(j, j + i));
            }
        }
        return res;
    }
}
