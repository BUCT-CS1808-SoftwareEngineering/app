package cn.edu.buct.se.cs1808.api;

import com.android.volley.Request;

public enum ApiPath {
    // 获取博物馆信息
    GET_MUSEUM_INFO("/api/museum/info", Request.Method.GET),

    // 非排序获取博物馆信息
    GET_ALL_MUSEUM_INFO("/api/museum/infoAll", Request.Method.GET),

    // 获取博物馆评分
    GET_MUSEUM_SCORE("/api/feedback/average",Request.Method.GET),

    // 获取藏品
    GET_COLLECTION("/api/collection",Request.Method.GET),

    // 获得展览列表
    GET_EXHIBITIONS("/api/exhibition", Request.Method.GET),

    // 获取教育活动信息
    GET_EDUCATION("/api/education",Request.Method.GET),

    // 获取评论
    GET_COMMENT("/api/comment", Request.Method.GET),

    // 获取用户评分
    GET_USER_SCORE("/api/feedback", Request.Method.GET),

    //获取新闻
    GET_NEWS_INFO("/api/museum/news",Request.Method.GET),

    // 创建用户验证Token，即登录
    CREATE_USER_AUTH_TOKEN("/api/login", Request.Method.POST),

    // 获得用户信息
    GET_USER_INFO("/api/user", Request.Method.GET),

    // 添加用户，即用户注册
    ADD_USER("/api/user", Request.Method.POST),

    // 获得讲解视频
    GET_VIDEO("/api/video", Request.Method.GET),

    // 上传讲解视频
    UPLOAD_VIDEO("/api/video", Request.Method.POST),

    // 修改用户信息
    CHANGE_USER_INFO("/api/user", Request.Method.PUT),

    // 获取关注的博物馆
    GET_CONCERNED_MUSEUMS("/api/attention", Request.Method.GET);


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
