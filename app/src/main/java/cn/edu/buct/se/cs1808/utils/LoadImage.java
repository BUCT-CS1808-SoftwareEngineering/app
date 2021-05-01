package cn.edu.buct.se.cs1808.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

public class LoadImage {
    private ImageView imageView;
    public LoadImage(ImageView imageView) {
        this.imageView = imageView;
    }

    public void setBitmap(String url) {
        AppThreadPool.getThreadPoolExecutor().execute(new LoadImageThread(url));
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

    private class LoadImageThread implements Runnable {
        private final String url;
        public LoadImageThread(String url) {
            this.url = url;
        }
        @Override
        public void run() {
            Bitmap bm = getImageBitMap(url);
            imageView.setImageBitmap(bm);
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

    private static Bitmap getImageBitMap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            Log.e("LoadImageError", e.toString());
        }
        return bm;
    }
}
