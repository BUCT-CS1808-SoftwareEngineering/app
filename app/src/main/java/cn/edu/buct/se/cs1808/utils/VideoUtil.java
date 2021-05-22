package cn.edu.buct.se.cs1808.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

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
    public static void setVideoDuration(String videoPath, VideoEvent event) {
        Handler handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                event.onDurationResponse(msg.arg1);
                return false;
            }
        });
        ThreadPoolExecutor pool = AppThreadPool.getThreadPoolExecutor();
        pool.execute(new VideoInfoThread(handler, videoPath));
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
