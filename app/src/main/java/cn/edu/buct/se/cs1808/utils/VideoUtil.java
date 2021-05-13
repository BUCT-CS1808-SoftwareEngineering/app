package cn.edu.buct.se.cs1808.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.util.HashMap;

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

}
