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
//            Log.i("ImageSize", String.format("%s, size: %d", url, data.length / 8));
            // 进行图片压缩，对大小在3MB以上的进行压缩
            if (data.length > 24576) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                bm = BitmapFactory.decodeByteArray(data, 0, data.length, options);
//                Log.i("ZipImage", String.format("%s, size: %d", url, bm.getByteCount() / 8));
            }
            else {
                bm = BitmapFactory.decodeByteArray(data, 0, data.length);
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
