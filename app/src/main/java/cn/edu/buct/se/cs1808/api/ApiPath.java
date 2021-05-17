package cn.edu.buct.se.cs1808.api;

import com.android.volley.Request;

public enum ApiPath {
    // 获取博物馆信息
    GET_MUSEUM_INFO("/api/museum/info", Request.Method.GET),

    // 创建用户验证Token，即登录
    CREATE_USER_AUTH_TOKEN("/api/login", Request.Method.POST),

    // 获得用户信息
    GET_USER_INFO("/api/user", Request.Method.GET),

    // 添加用户，即用户注册
    ADD_USER("/api/user", Request.Method.POST);

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
