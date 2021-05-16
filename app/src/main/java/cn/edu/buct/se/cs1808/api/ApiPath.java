package cn.edu.buct.se.cs1808.api;

import com.android.volley.Request;

public enum ApiPath {
    // 获取博物馆信息
    GET_MUSEUM_INFO("/api/museum/info", Request.Method.GET);

    private final String path;
    private final int method;
    private ApiPath(String path, int method) {
        this.path = path;
        this.method = method;
    }
    public String getPath() {
        return path;
    }
    public int getMethod() {
        return method;
    }
}
