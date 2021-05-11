package cn.edu.buct.se.cs1808;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.edu.buct.se.cs1808.components.VideoListItem;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class VideoIntroduceActivity extends AppCompatActivity {
    private LinearLayout listArea;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videointrouduce);

        ImageView backButton = (ImageView) findViewById(R.id.videoIntroduceBackButt);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoIntroduceActivity.this.finish();
            }
        });
        ImageView gotoUploadedPageButton = (ImageView) findViewById(R.id.gotoMineUploadedButton);
        gotoUploadedPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoIntroduceActivity.this, UserUploadedVideoActivity.class);
                startActivity(intent);
            }
        });

        listArea = (LinearLayout) findViewById(R.id.videoListLayout);

        ImageView searchButton = (ImageView) findViewById(R.id.searchVideoButton);
        EditText searchInput = (EditText) findViewById(R.id.videoSearchInput);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = searchInput.getText().toString();
                search(q);
            }
        });
    }

    private void search(String q) {
        if (q == null || q.length() == 0) return;
        Log.i("Search", q);
    }
    @Override
    protected void onStart() {
        super.onStart();
        // 测试动态添加
        addItem("北京博物馆介绍", "essay", "13:14", "2021-05-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
        addItem("故宫博物馆介绍", "czx", "13:14", "2021-06-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
        addItem("国家博物馆介绍", "bleafumb", "13:14", "2021-05-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
        addItem("Introduce", "末老师", "13:14", "2021-05-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
        addItem("北京博物馆介绍", "essay", "13:14", "2021-05-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
        addItem("故宫博物馆介绍", "czx", "13:14", "2021-06-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
        addItem("国家博物馆介绍——某藏品介绍", "bleafumb", "13:14", "2021-05-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
        addItem("Introduce of Beijing Museum", "末老师", "2:13:14", "2021-05-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
    }

    private void addItem(String title, String user, String time, String uploadTime, String imageSrc) {
        VideoListItem item = new VideoListItem(this);
        listArea.addView(item);
        RoundView.setRadiusWithDp(12, item);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) item.getLayoutParams();
        lp.setMargins(0, 0, 0, 32);
        item.setLayoutParams(lp);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        item.setAttr(title, user, time, uploadTime, imageSrc);
    }
}
