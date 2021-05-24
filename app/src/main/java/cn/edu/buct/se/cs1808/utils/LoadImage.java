package cn.edu.buct.se.cs1808.utils;

import android.annotation.SuppressLint;
import android.content.Context;
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
import java.io.File;
import java.io.FileOutputStream;
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

    private boolean useCache = true;
    private Context context;
    public LoadImage(ImageView imageView) {
        context = imageView.getContext();
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
    public LoadImage(ImageView imageView, boolean useCache) {
        this(imageView);
        this.useCache = useCache;
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
            // 检查缓存中是否存在
            if (useCache) {
                Bitmap bm = getFromCache(url);
                if (bm != null) {
                    Log.i("UseCache", url);
                    LoadImage.this.bm = bm;
                    handler.sendEmptyMessage(0);
                    return;
                }
            }
            LoadImage.this.bm = getImageBitMap(url);
            // 存储缓存
            storeCache(LoadImage.this.bm, url);
            handler.sendEmptyMessage(0);
        }

        /**
         * 从缓存中加载图片
         * @param url 图片网络路径
         * @return 加载到的缓存文件，没有则为null
         */
        private Bitmap getFromCache(String url) {
            String filename = MD5.encode(url);
            File file = new File(context.getCacheDir(), filename);
            if (file.exists()) {
                return BitmapFactory.decodeFile(file.getAbsolutePath());
            }
            return null;
        }

        /**
         * 存储缓存文件
         * @param bm 位图文件
         * @param url 图片网络路径
         */
        private void storeCache(Bitmap bm, String url) {
            String filename = MD5.encode(url);
            File file = new File(context.getCacheDir(), filename);
            // 防止多个同时请求相同图片，存储缓存时对同一个文件进行写操作（我也不知道有没有用
            synchronized (LoadImageThread.class) {
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    Log.e("StoreCacheFail", e.getMessage());
                }
            }
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
