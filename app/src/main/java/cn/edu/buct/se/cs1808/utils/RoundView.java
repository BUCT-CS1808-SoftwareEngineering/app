package cn.edu.buct.se.cs1808.utils;

import android.graphics.Outline;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewOutlineProvider;


public class RoundView {
    public static void setRadius(int radius, View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
                }
            });
            view.setClipToOutline(true);
        }
    }

    public static void setRadiusWithDp(int radius, View view) {
        radius = DensityUtil.dip2px(view.getContext(), radius);
        setRadius(radius, view);
    }

    public static void setCircleRadius(View view) {
        int radius = Math.min(view.getWidth(), view.getHeight());
        Log.i("Radius", String.valueOf(radius));
        setRadiusWithDp(radius, view);
    }
}
