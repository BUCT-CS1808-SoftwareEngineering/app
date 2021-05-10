package cn.edu.buct.se.cs1808;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.edu.buct.se.cs1808.utils.RoundView;

public class UploadVideoActivity extends AppCompatActivity {
    private TextView publicButton;
    private ImageView backButton;
    private EditText introduceInput;
    private VideoView selectedVideo;
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
        selectedVideo = (VideoView) findViewById(R.id.selectedVideo);
        videoTitleInput = (EditText) findViewById(R.id.videoTitleInput);
        selectedMuseumName = (TextView) findViewById(R.id.selectedMuseumName);
        selectMuseumButt = (TextView) findViewById(R.id.selectMuseumButt);
        initRadius();
    }

    private void initRadius() {
        int radius = 12;
        RoundView.setRadiusWithDp(radius, publicButton);
        RoundView.setRadiusWithDp(radius, selectedVideo);
    }

}
