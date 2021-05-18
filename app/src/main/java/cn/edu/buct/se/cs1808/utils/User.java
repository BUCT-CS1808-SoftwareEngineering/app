package cn.edu.buct.se.cs1808.utils;

import android.content.Context;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import cn.edu.buct.se.cs1808.LoginPageActivity;
import cn.edu.buct.se.cs1808.RegisterPageActivity;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;

public class User {
    private static final String USER_FILENAME = "user.json";

    /**
     * 跳转到登录页面
     * @param context 应用上下文
     */
    public static void gotoLoginPage(Context context) {
        Intent intent = new Intent(context, LoginPageActivity.class);
        context.startActivity(intent);
    }

    /**
     * 跳转到注册页面
     * @param context 应用上下文
     */
    public static void gotoRegisterPage(Context context) {
        Intent intent = new Intent(context, RegisterPageActivity.class);
        context.startActivity(intent);
    }

    /**
     * 判断用户是否登录
     * @param context 应用上下文
     * @return 布尔值，代表是否登陆
     */
    public static boolean isLogin(Context context) {
        return getUserInfo(context) != null;
    }

    /**
     * 获得用户登录信息
     * @param context 应用上下文
     * @return 用户信息的json数据，若没有登录或者登录状态过期返回null
     */
    public static JSONObject getUserInfo(Context context) {
        JSONObject info = JsonFileHandler.readJsonObject(context, USER_FILENAME);
        if (info == null) {
            return null;
        }
        try {
            String token = info.getString("token");
            if (parseToken(token) == null) {
                // 登陆状态过期或者token不合法
                return null;
            }
        }
        catch (JSONException e) {
            return null;
        }
        return info;
    }


    /**
     * 通过新的Token更新本地存储的登陆状态，以及存储的用户的信息
     * @param token 登录获取的Token
     */
    public static void updateLoginStatus(Context context, String token, Event eventHandler) {
        JSONObject payload = parseToken(token);
        if (payload == null) {
            eventHandler.onStatusUpdated(null);
            return;
        }
        int id = -1;
        try {
            id = payload.getInt("id");
        }
        catch (JSONException e) {
            eventHandler.onStatusUpdated(null);
            return;
        }
        JSONObject userInfo = new JSONObject();
        try {
            userInfo.put("token", token);
        }
        catch (JSONException e) {
            eventHandler.onStatusUpdated(null);
            return;
        }
        JSONObject params = new JSONObject();
        try {
            // 由于目前接口只能以分页形式查询
            // 不能通过指定ID的形式查询，因此指定一个较大的页进行查询
            params.put("pageIndex", 1);
            params.put("pageSize", 100000);
//            params.put("user_ID", id);
        }
        catch (JSONException e) {
            eventHandler.onStatusUpdated(null);
            return;
        }
        int currentUserId = id;
        ApiTool.request(context, ApiPath.GET_USER_INFO, params, (JSONObject rep) -> {
            try {
                String code = rep.getString("code");
                if (!"success".equals(code)) {
                    eventHandler.onStatusUpdated(null);
                    return;
                }
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                for (int i = 0; i < items.length(); i ++) {
                    JSONObject item = items.getJSONObject(i);
                    int userId = item.getInt("user_ID");
                    if (userId != currentUserId)  {
                        // 由于目前接口不能通过ID查询用户信息
                        // 需要查询一定范围的用户信息然后遍历结果
                        // 找到与登录时候获取的token对应的用户id
                        continue;
                    }
                    item.put("token", token);
                    if (JsonFileHandler.write(context, USER_FILENAME, item.toString(), StandardCharsets.UTF_8, Context.MODE_PRIVATE)) {
                        eventHandler.onStatusUpdated(item);
                    }
                    else {
                        // 如果写入失败，则当作更新Token失败
                        // 因为后面无法使用该token以及判断用户是否已经登录
                        eventHandler.onStatusUpdated(null);
                    }
                    break;
                }
            }
            catch (JSONException e) {
                eventHandler.onStatusUpdated(null);
            }
        }, (JSONObject error) -> {
            eventHandler.onStatusUpdated(null);
        });
    }

    /**
     * 返回用户登录获取到的token
     * @param context 应用上下文
     * @return token字符串，若不存在则返回null
     */
    public static String getToken(Context context) {
        JSONObject userInfo = getUserInfo(context);
        if (userInfo == null) return null;
        try {
            return userInfo.getString("token");
        }
        catch (JSONException e) {
            return null;
        }
    }

    /**
     * 解析JWT Token
     * @param token token
     * @return 若解析成功，返回payload，若token不合法或者过期，则返回null
     */
    public static JSONObject parseToken(String token) {
        if (token == null || token.length() == 0) {
            return null;
        }
        String[] items = token.split("\\.");
        if (items.length != 3) {
            // JWT Token通过. 分割后一定有三部分
            return null;
        }
        // 所有数据都在第二部分中
        String payload = Base64.decodeToString(items[1]);
        if (payload.length() == 0) {
            // 解密失败
            return null;
        }
        try {
            JSONObject payloadJson = new JSONObject(payload);
            long exp = payloadJson.getLong("exp");
            long now = (new Date()).getTime();
            // Token中的exp单位是秒
            now /= 1000;
            if (exp <= now) {
                // 登录状态过期
                return null;
            }
            payloadJson.remove("exp");
            payloadJson.remove("iat");
            return payloadJson;
        }
        catch (JSONException e) {
            return null;
        }
    }

    /**
     * 退出登录
     * @param context 应用上下文
     */
    public static void logout(Context context) {
        File file = new File(context.getFilesDir(), USER_FILENAME);
        if (file.exists()) {
            file.delete();
        }
    }
    public static interface Event {
        public void onStatusUpdated(JSONObject userStatus);
    }

}
