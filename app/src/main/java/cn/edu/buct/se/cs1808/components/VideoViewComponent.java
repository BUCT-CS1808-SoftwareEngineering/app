package cn.edu.buct.se.cs1808.components;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.VideoView;

import cn.edu.buct.se.cs1808.utils.VideoUtil;

public class VideoViewComponent extends VideoView {
    public VideoViewComponent(Context context) {
        super(context);
    }

    public VideoViewComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void start() {
        this.setBackground(null);
        super.start();
    }

    @Override
    public void setVideoURI(Uri uri) {
        super.setVideoURI(uri);
    }

    @Override
    public void setVideoPath(String path) {
        super.setVideoPath(path);
    }

    public void setVideoImage(String path) {
        Bitmap image = VideoUtil.getFirstImage(path);
        setBackground(new BitmapDrawable(image));
    }

    public void setVideoImage(Uri uri) {
        Bitmap image = VideoUtil.getFirstImage(getContext(), uri);
        setBackground(new BitmapDrawable(image));
    }

}
