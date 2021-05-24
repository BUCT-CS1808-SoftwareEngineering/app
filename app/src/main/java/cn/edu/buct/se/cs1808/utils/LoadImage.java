package cn.edu.buct.se.cs1808.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
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
            // 某些图片缺少该参数无法加载
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36 Edg/90.0.818.66");
            conn.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
            conn.setRequestProperty("Accept", "*/*");
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int len = 0;
            while((len= is.read(buffer)) != -1 ){
                outStream.write(buffer, 0, len);
            }
            is.close();
            byte[] data =  outStream.toByteArray();
            // 统一压缩到360P
            bm = getImageThumbnail(data, 480, 460);
        }
        catch (Exception e) {
            Log.e("LoadImageError", e.toString());
        }
        return bm;
    }
    private static Bitmap getImageThumbnail(byte[] data, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // 获取这个图片的宽和高，注意此处的bitmap为null
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inJustDecodeBounds = false; // 设为 false
        // 计算缩放比
        int h = options.outHeight;
        int w = options.outWidth;
        int beWidth = w / width;
        int beHeight = h / height;
        int be = 1;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;
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
