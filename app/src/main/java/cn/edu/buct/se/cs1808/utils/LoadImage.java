package cn.edu.buct.se.cs1808.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import cn.edu.buct.se.cs1808.R;

public class LoadImage {
    private Bitmap bm;
    Handler handler;
    public LoadImage(ImageView imageView) {
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                if (bm == null){
                    imageView.setImageResource(R.drawable.common_error);
                }
                else{
                    imageView.setImageBitmap(bm);
                }
                return true;
            }
        });
    }

    public void setBitmap(String url) {
        AppThreadPool.getThreadPoolExecutor().execute(new LoadImageThread(url, handler));
    }

    public static Bitmap getBitmap(String url) {
        Future<Bitmap> futureTask = AppThreadPool.getThreadPoolExecutor().submit(new GetImageThread(url));
        Bitmap res = null;
        try {
            res = futureTask.get();
        }
        catch (Exception e) {
            Log.e("ImageLoadError", e.getMessage());
        }
        return res;
    }

    private static Bitmap getImageBitMap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;
            // 某些图片缺少该参数无法加载
            http.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36 Edg/90.0.818.66");
            http.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            http.setRequestProperty("Accept", "*/*");

            conn.connect();
            // 获得图像的字符流
            try (InputStream is = conn.getInputStream()) {
                bm = BitmapFactory.decodeStream(is);
            }
        }
        catch (Exception e) {
            Log.e("LoadImageError", e.toString());
        }
        return bm;
    }
    private class LoadImageThread implements Runnable {
        private final String url;
        private final Handler handler;
        public LoadImageThread(String url, Handler handler) {
            this.url = url;
            this.handler = handler;
        }
        @Override
        public void run() {
            LoadImage.this.bm = getImageBitMap(url);
            handler.sendEmptyMessage(0);
        }
    }

    private static class GetImageThread implements Callable<Bitmap> {
        private final String url;
        public GetImageThread(String url) {
            this.url = url;
        }
        @Override
        public Bitmap call() throws Exception {
            return getImageBitMap(url);
        }
    }

}
