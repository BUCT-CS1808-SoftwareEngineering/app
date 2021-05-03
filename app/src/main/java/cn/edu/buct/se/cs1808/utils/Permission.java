package cn.edu.buct.se.cs1808.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class Permission {
    // 不知什么含义, 貌似可以用来在授权后的回调中获得
    public static int REQUEST_CODE = 10;

    /**
     * 检查是否有某项权限
     * @param context 上下文context
     * @param permission 检查的权限
     * @return 是否有
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static boolean check(Context context, String permission) {
        return PackageManager.PERMISSION_GRANTED == context.checkSelfPermission(permission);
    }

    /**
     * 通过activity请求权限
     * @param activity 当前的activity
     * @param permission 请求的权限字符串
     */
    public static void request(Activity activity, String permission) {
        ActivityCompat.requestPermissions(activity, new String[] {
                permission
        }, REQUEST_CODE);
    }

    /**
     * 通过activity请求权限
     * @param activity 当前的activity
     * @param permissions 请求的权限字符串
     */
    public static void requestAll(Activity activity, String[] permissions) {
        ActivityCompat.requestPermissions(activity, permissions, REQUEST_CODE);
    }

    /**
     * 通过fragment请求权限
     * @param fragment 当前的fragment
     * @param permission 请求的权限字符串
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void request(Fragment fragment, String permission) {
        fragment.requestPermissions(new String[] {
                permission
        }, REQUEST_CODE);
    }

    /**
     * 通过fragment请求权限
     * @param fragment 当前的fragment
     * @param permissions 请求的权限字符串
     */
    public static void requestAll(Fragment fragment, String[] permissions) {
        fragment.requestPermissions(permissions, REQUEST_CODE);
    }

}
