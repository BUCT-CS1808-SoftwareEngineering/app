package cn.edu.buct.se.cs1808.api;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;

import cn.edu.buct.se.cs1808.utils.FileEntity;
import cn.edu.buct.se.cs1808.utils.User;


public final class ApiTool {
    private ApiTool() {}

    /**
     * API 地址
     */
    private static final String ADDRESS = "http://149.129.54.32:8081";
    private static final JSONObject headers = new JSONObject();

    public static String getADDRESS() {
        return ADDRESS;
    }

    public static void request(Context context, ApiPath api, JSONObject params, RequestListener success, RequestListener error) {
        beforeRequest(context);
        int method = api.getMethod();
        String apiPath = api.getPath();
        apiPath = ADDRESS + apiPath;
        Handler successHandler = new Handler((Message msg) -> {
            if (msg.obj instanceof JSONObject) {
                success.onResponse((JSONObject) msg.obj);
                return true;
            }
            else {
                success.onResponse(null);
            }
            return false;
        });
        Handler errorHandler = new Handler((Message msg) -> {
            if (msg.obj instanceof VolleyError) {
                VolleyError repError = (VolleyError) msg.obj;
                JSONObject errorJson = new JSONObject();
                try {
                    errorJson.put("message", repError.getMessage());
                    errorJson.put("networkTimeMs", repError.getNetworkTimeMs());
                    errorJson.put("localizedMessage", repError.getLocalizedMessage());
                    errorJson.put("info", repError.toString());
                    errorJson.put("body", new String(repError.networkResponse.data, StandardCharsets.UTF_8));
                }
                catch (JSONException ignore) {
                    errorJson = null;
                }
                error.onResponse(errorJson);
                return true;
            }
            else {
                error.onResponse(null);
            }
            return false;
        });
        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Message message = new Message();
                message.obj = (Object) response;
                successHandler.sendMessage(message);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message message = new Message();
                message.obj = (Object) error;
                errorHandler.sendMessage(message);
            }
        };

        switch (method) {
            case Request.Method.GET:
                HttpRequest.getInstance(context).get(apiPath, params, headers, successListener, errorListener);
                break;
            case Request.Method.POST:
                HttpRequest.getInstance(context).post(apiPath, params, headers, successListener, errorListener);
                break;
            case Request.Method.PUT:
                HttpRequest.getInstance(context).put(apiPath, params, headers, successListener, errorListener);
                break;
            case Request.Method.DELETE:
                HttpRequest.getInstance(context).delete(apiPath, params, headers, successListener, errorListener);
                break;
        }
    }


    /**
     * 文件上传请求
     * @param context 应用上下文
     * @param api API地址
     * @param params 参数
     * @param file 需要上传的文件
     * @param success 成功回调
     * @param error 失败回调
     */
    public static void request(Context context, ApiPath api, JSONObject params, FileEntity file, RequestListener success, RequestListener error) {
        beforeRequest(context);
        String apiPath = api.getPath();
        apiPath = ADDRESS + apiPath;
        Handler successHandler = new Handler((Message msg) -> {
            if (msg.obj instanceof JSONObject) {
                success.onResponse((JSONObject) msg.obj);
                return true;
            }
            else {
                success.onResponse(null);
            }
            return false;
        });
        Handler errorHandler = new Handler((Message msg) -> {
            if (msg.obj instanceof VolleyError) {
                VolleyError repError = (VolleyError) msg.obj;
                JSONObject errorJson = new JSONObject();
                try {
                    errorJson.put("message", repError.getMessage());
                    errorJson.put("networkTimeMs", repError.getNetworkTimeMs());
                    errorJson.put("localizedMessage", repError.getLocalizedMessage());
                    errorJson.put("info", repError.toString());
                    errorJson.put("body", new String(repError.networkResponse.data, StandardCharsets.UTF_8));
                }
                catch (JSONException ignore) {
                    errorJson = null;
                }
                error.onResponse(errorJson);
                return true;
            }
            else {
                error.onResponse(null);
            }
            return false;
        });
        Response.Listener<JSONObject> successListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Message message = new Message();
                message.obj = (Object) response;
                successHandler.sendMessage(message);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Message message = new Message();
                message.obj = (Object) error;
                errorHandler.sendMessage(message);
            }
        };
        HttpRequest.getInstance(context).fileRequest(apiPath, params, file, headers, successListener, errorListener);
    }
    private static void beforeRequest(Context context) {
        StringBuilder value = new StringBuilder("Bearer ");
        String token = User.getToken(context);
        if (token == null) return;
        value.append(token);
        try {
            headers.put("Authorization", value.toString());
        }
        catch (JSONException ignore) {
        }
    }


    public static interface RequestListener {
        public void onResponse(JSONObject rep);
    }
}
