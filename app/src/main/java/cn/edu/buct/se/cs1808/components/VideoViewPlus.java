package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;


import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.utils.DensityUtil;
import cn.edu.buct.se.cs1808.utils.LoadImage;

public class VideoViewPlus extends LinearLayout {
    private View view;
    private VideoViewComponent videoViewComponent;
    private int verticalHeight = 320;
    public VideoViewPlus(Context context) {
        super(context);
        init(context);
    }
    public VideoViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_video_view_plus, this);

        LinearLayout layout = (LinearLayout) findViewById(R.id.videoViewArea);
        videoViewComponent = (VideoViewComponent) findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(context);
        videoViewComponent.setMediaController(mediaController);
        videoViewComponent.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int height = mp.getVideoHeight();
                int width = mp.getVideoWidth();
                if (height > width) {
                    ViewGroup.LayoutParams lp = view.getLayoutParams();
                    lp.height = DensityUtil.dip2px(context, verticalHeight);
                    view.setLayoutParams(lp);
                }
            }
        });
    }

    public void play(String path) {
        Log.i("PlayVideo", path);
        videoViewComponent.setVideoPath(path);
    }
    public void play(Uri uri) {
        videoViewComponent.setVideoURI(uri);
    }
    public void setVideoFirstImage(String path) {
        videoViewComponent.setVideoFirstImage(path);
    }
    public void setVideoFirstImage(Uri uri) {
        videoViewComponent.setVideoFirstImage(uri);
    }

    public void setImage(String path) {
        Bitmap bitmap = LoadImage.getBitmap(path);
        setImage(bitmap);
    }
    public void setImage(Bitmap bitmap) {
        videoViewComponent.setBackground(new BitmapDrawable(bitmap));
    }

    public void setVerticalHeight(int height) {
        verticalHeight = height;
    }

}
