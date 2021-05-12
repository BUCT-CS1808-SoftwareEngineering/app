package cn.edu.buct.se.cs1808.api;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.edu.buct.se.cs1808.utils.FileEntity;
import cn.edu.buct.se.cs1808.utils.JsonHelper;
import cn.edu.buct.se.cs1808.utils.MultipartRequest;


public class HttpRequest {

    private volatile static HttpRequest instance;
    private final RequestQueue requestQueue;
    private static final Map<String, String> DEFAULT_HEADERS;

    static {
        DEFAULT_HEADERS = new HashMap<>();
    }

    private HttpRequest(Context ctx) {
        requestQueue = Volley.newRequestQueue(ctx);
    }
    public static HttpRequest getInstance(Context ctx) {
        if (instance == null) {
            synchronized (HttpRequest.class) {
                if (instance == null) {
                    instance = new HttpRequest(ctx);
                }
            }
        }
        return instance;
    }

    /**
     * 发送一个GET请求
     * @param url 请求地址
     * @param params 请求参数
     * @param headers 请求头
     * @param successCallback 成功回调，回调参数为JSONObject
     * @param errorCallback 失败回调，回调为VolleyError
     */
    public void get(String url, JSONObject params, JSONObject headers,
                    Response.Listener<JSONObject> successCallback,
                    Response.ErrorListener errorCallback
    ) {
        StringBuilder paramsURL = new StringBuilder(url);
        paramsURL.append("?");
        try {
            for (Iterator<String> it = params.keys(); it.hasNext(); ) {
                String key = it.next();
                String value = params.getString(key);
                key = URLEncoder.encode(key, "UTF-8");
                value = URLEncoder.encode(value, "UTF-8");
                paramsURL.append(key);
                paramsURL.append("=");
                paramsURL.append(value);
                paramsURL.append("&");
            }
        }
        catch (JSONException | UnsupportedEncodingException ignored) {
        } finally {
            url = paramsURL.toString();
        }
        JsonObjectRequest jsonObjectRequest = null;
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, successCallback, errorCallback) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers == null) {
                    return DEFAULT_HEADERS;
                }
                return toMap(headers);
            }
        };
        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);
    }


    /**
     * 发送POST请求
     * @param url 请求地址
     * @param params 请求的参数
     * @param headers 请求头
     * @param successCallback 请求成功回调，回调传参JSONObject对象
     * @param errorCallback 请求失败回调，回调传参VolleyError
     */
    public void post(String url, JSONObject params, JSONObject headers,
                     Response.Listener<JSONObject> successCallback,
                     Response.ErrorListener errorCallback)
    {
        requestWithBody(url, Request.Method.POST, params, headers, successCallback, errorCallback);
    }

    /**
     * 发送PUT请求
     * @param url 请求地址
     * @param params 请求的参数
     * @param headers 请求头
     * @param successCallback 请求成功回调，回调传参JSONObject对象
     * @param errorCallback 请求失败回调，回调传参VolleyError
     */
    public void put(String url, JSONObject params, JSONObject headers,
                     Response.Listener<JSONObject> successCallback,
                     Response.ErrorListener errorCallback)
    {
        requestWithBody(url, Request.Method.PUT, params, headers, successCallback, errorCallback);
    }
    /**
     * 发送DELETE请求
     * @param url 请求地址
     * @param params 请求的参数
     * @param headers 请求头
     * @param successCallback 请求成功回调，回调传参JSONObject对象
     * @param errorCallback 请求失败回调，回调传参VolleyError
     */
    public void delete(String url, JSONObject params, JSONObject headers,
                    Response.Listener<JSONObject> successCallback,
                    Response.ErrorListener errorCallback)
    {
        requestWithBody(url, Request.Method.DELETE, params, headers, successCallback, errorCallback);
    }

    /**
     * 以POST方法上传文件以及参数
     * @param url 接口地址
     * @param params 参数
     * @param file 文件
     * @param headers 请求头
     * @param successCallback 成功回调
     * @param errorCallback 失败回调
     */
    public void fileRequest(String url, JSONObject params, FileEntity file, JSONObject headers,
                         Response.Listener<JSONObject> successCallback,
                         Response.ErrorListener errorCallback)
    {
        MultipartRequest fileRequest = null;
        Map<String, String> mapParams = toMap(params);
        fileRequest = new MultipartRequest(url, mapParams, file, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    successCallback.onResponse(new JSONObject(response));
                } catch (JSONException e) {
                    successCallback.onResponse(null);
                }
            }
        }, errorCallback) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers == null) {
                    return DEFAULT_HEADERS;
                }
                return toMap(headers);
            }
        };
        fileRequest.setShouldCache(false);
        requestQueue.add(fileRequest);
    }

    /**
     * 发送一个带请求体的请求
     * @param url 地址
     * @param method 请求方法(POST, PUT, DELETE)
     * @param body 请求体，为JSON对象
     * @param headers 请求头
     * @param successCallback 请求成功回调
     * @param errorCallback 请求失败回调
     */
    public void requestWithBody(String url, int method, JSONObject body, JSONObject headers,
                                 Response.Listener<JSONObject> successCallback,
                                 Response.ErrorListener errorCallback)
    {
        JsonObjectRequest jsonObjectRequest = null;
        jsonObjectRequest = new JsonObjectRequest(method, url, body, successCallback, errorCallback) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (headers == null) {
                    return DEFAULT_HEADERS;
                }
                return toMap(headers);
            }
        };
        jsonObjectRequest.setShouldCache(false);
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * 设置默认请求头
     * @param key 请求头名称
     * @param value 请求头值
     */
    public static void setHeader(String key, String value) {
        DEFAULT_HEADERS.put(key, value);
    }

    /**
     * 将JSONObject转化为Map<String, String>
     * @param json JSONObject数据
     * @return 结果
     */
    private Map<String, String> toMap(JSONObject json) {
        if (json == null) {
            return new HashMap<>();
        }
        Map<String, Object> map = null;
        try {
            map = JsonHelper.toMap(json);
        } catch (JSONException e) {
            return new HashMap<>();
        }
        Map<String, String> res = new HashMap<>();
        for (String key : map.keySet()) {
            Object value = map.get(key);
            if (value instanceof  String) {
                res.put(key, (String) value);
            }
        }
        return res;
    }
}
