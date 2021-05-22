package cn.edu.buct.se.cs1808.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class VideoUtil {
    public static Bitmap getFirstImage(Context context, Uri uri) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(context, uri);
        return retriever.getFrameAtTime();
    }

    public static Bitmap getFirstImage(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path, new HashMap<>());
        return retriever.getFrameAtTime();
    }

    /**
     * 获取视频时长
     * @param videoPath 视频地址
     * @param event 回调函数
     */
    public static void setVideoDuration(Context context, String videoPath, VideoEvent event) {
        int res = getVideoCachedDuration(context, videoPath);
        if (res != -1) {
            // 缓存命中
            event.onDurationResponse(res);
            return;
        }
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                setCache(context, videoPath, msg.arg1);
                event.onDurationResponse(msg.arg1);
                return false;
            }
        });
        ThreadPoolExecutor pool = AppThreadPool.getThreadPoolExecutor();
        pool.execute(new VideoInfoThread(handler, videoPath));
    }
    private static final String CACHE_FILENAME = "video_duration_cache.json";
    /**
     * 从缓存中获取video的时间信息
     * @param context 应用上下文
     * @param videoPath 视频路径
     * @return 时间, -1代表无缓存
     */
    public static int getVideoCachedDuration(Context context, String videoPath) {
        JSONObject cache = JsonFileHandler.readJsonObject(context, CACHE_FILENAME);
        if (cache == null) {
            return -1;
        }
        String key = MD5.encode(videoPath);
        if (!cache.has(key)) {
            return -1;
        }
        try {
            return cache.getInt(key);
        }
        catch (JSONException e) {
            return -1;
        }
    }

    /**
     * 将结果放到缓存中
     * @param context 应用上下文
     * @param videoPath 视频路径
     * @param res 内容
     */
    private static void setCache(Context context, String videoPath, int res) {
        JSONObject cache = JsonFileHandler.readJsonObject(context, CACHE_FILENAME);
        if (cache == null) cache = new JSONObject();
        String key = MD5.encode(videoPath);
        try {
            cache.put(key, String.valueOf(res));
        }
        catch (JSONException ignore) {
            return;
        };
        JsonFileHandler.write(context, CACHE_FILENAME, cache.toString(), StandardCharsets.UTF_8, Context.MODE_PRIVATE);
    }

    /**
     * 视频的时长（秒）转化为 xx:xx:xx 的形式
     * @param duration 视频时长
     * @return 结果
     */
    public static String durationSecToString(int duration) {
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        seconds = duration % 60;
        minutes += duration / 60;
        hours += minutes / 60;
        minutes %= 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * video信息获取回调
     */
    public static interface VideoEvent {
        public void onDurationResponse(int duration);
    }

    /**
     * 获取video信息的线程
     */
    private static class VideoInfoThread implements Runnable {
        private final Handler event;
        private final String path;
        VideoInfoThread(Handler event, String path) {
            this.event = event;
            this.path = path;
        }
        @Override
        public void run() {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(path, new HashMap<>());
            String duration = retriever.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION);
            Message message = new Message();
            message.arg1 = Integer.parseInt(duration) / 1000;
            event.sendMessage(message);
        }
    }

}
