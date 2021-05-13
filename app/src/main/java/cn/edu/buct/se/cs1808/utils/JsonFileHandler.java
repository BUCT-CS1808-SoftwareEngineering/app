package cn.edu.buct.se.cs1808.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class JsonFileHandler {
    /**
     * 从文件中读取数据
     * @param context 应用上下文
     * @param filename 文件名
     * @return 读取的内容字符串
     */
    public static String read(Context context, String filename, Charset charset) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(filename);
        }
        catch (FileNotFoundException e) {
            Log.e("File IO", filename + " not found!");
            return null;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(fis, charset);
        StringBuilder contentBuilder = new StringBuilder();
        String content = null;
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                contentBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        }
        catch (IOException e) {
            return null;
        }
        finally {
            content = contentBuilder.toString();
        }
        return content;
    }

    /**
     * 从文件中读取JSONObject数据并解析
     * @param context 应用上下文
     * @param filename 文件名
     * @return 解析后的数据
     */
    public static JSONObject readJsonObject(Context context, String filename) {
        String content = read(context, filename, StandardCharsets.UTF_8);
        if (content == null) return null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(content);
        }
        catch (JSONException e) {
            jsonObject = null;
            Log.e("JSONError", "JSON parse error!\n" + e.getMessage());
        };
        return jsonObject;
    }

    /**
     * 从文件中读取JSONArray数据并解析
     * @param context 应用上下文
     * @param filename 文件名
     * @return 解析后的数据
     */
    public static JSONArray readJsonArray(Context context, String filename) {
        String content = read(context, filename, StandardCharsets.UTF_8);
        if (content == null) return null;
        JSONArray jsonArray;
        try {
            jsonArray = new JSONArray(content);
        }
        catch (JSONException e) {
            jsonArray = null;
            Log.e("JSONError", "JSON parse error!\n" + e.getMessage());
        };
        return jsonArray;
    }

    /**
     * 写文件
     * @param context 应用上下文
     * @param filename 文件名
     * @param content 内容
     * @param charset 编码
     * @param mode 写入模式
     * @return 是否成功
     */
    public static boolean write(Context context, String filename, String content, Charset charset, int mode) {
        FileOutputStream fos;
        try {
             fos = context.openFileOutput(filename, mode);
        }
        catch (FileNotFoundException e) {
            return false;
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(fos, charset)) {
            writer.write(content);
        }
        catch (IOException e) {
            Log.e("Write", filename + " write fail!");
            return false;
        }
        return true;
    }
}
