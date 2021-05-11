package cn.edu.buct.se.cs1808;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

import cn.edu.buct.se.cs1808.components.VideoViewPlus;
import cn.edu.buct.se.cs1808.utils.DensityUtil;
import cn.edu.buct.se.cs1808.utils.RoundView;
import cn.edu.buct.se.cs1808.utils.UriToFilePath;
import cn.edu.buct.se.cs1808.utils.VideoUtil;

public class UploadVideoActivity extends AppCompatActivity {
    private TextView publicButton;
    private ImageView backButton;
    private EditText introduceInput;
    private VideoViewPlus selectedVideo;
    private EditText videoTitleInput;
    private TextView selectedMuseumName;
    private TextView selectMuseumButt;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        publicButton = (TextView) findViewById(R.id.publicButton);
        backButton = (ImageView) findViewById(R.id.backButton);
        introduceInput = (EditText) findViewById(R.id.introduceInput);
        selectedVideo = (VideoViewPlus) findViewById(R.id.selectedVideo);
        selectedVideo.setVerticalHeight(400);
        videoTitleInput = (EditText) findViewById(R.id.videoTitleInput);
        selectedMuseumName = (TextView) findViewById(R.id.selectedMuseumName);
        selectMuseumButt = (TextView) findViewById(R.id.selectMuseumButt);
        initRadius();

        Intent intent = getIntent();
        if (intent != null) {
            String path = intent.getStringExtra("path");
            selectedVideo.play(path);
            selectedVideo.setVideoImage(path);
        }
    }

    private void initRadius() {
        int radius = 12;
        RoundView.setRadiusWithDp(radius, publicButton);
        RoundView.setRadiusWithDp(radius, selectedVideo);
    }

}
