package cn.edu.buct.se.cs1808;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    private List<String> museumList;
    private int selectedMuseumId;

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
            Uri uri = Uri.parse(path);
            selectedVideo.setVideoImage(uri);
            selectedVideo.play(uri);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadVideoActivity.this.finish();
            }
        });

        OptionsPickerView<String> pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                selectMuseum(options1);
            }
        }).setSubmitColor(Color.WHITE).setCancelColor(Color.WHITE)
                .setSubmitText("选择")
                .setSelectOptions(0)
                .setTitleText("选择博物馆")
                .setTitleColor(Color.BLACK)
                .build();
        pvOptions.setPicker(museumList = loadMuseumList());
        selectMuseum(0);
        selectMuseumButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvOptions.show();
            }
        });
    }

    private void initRadius() {
        int radius = 12;
        RoundView.setRadiusWithDp(radius, publicButton);
        RoundView.setRadiusWithDp(radius, selectedVideo);
    }

    private List<String> loadMuseumList() {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < 23; i ++) {
            res.add("测试博物馆" + i + "号");
        }
        return res;
    }

    private void selectMuseum(int id) {
        if (museumList == null) return;
        if (id < 0 || id >= museumList.size()) return;
        selectedMuseumId = id;
        selectedMuseumName.setText(museumList.get(id));
    }

}
