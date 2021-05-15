package cn.edu.buct.se.cs1808;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.edu.buct.se.cs1808.components.VideoListItem;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class VideoPlayActivity extends AppCompatActivity {
    private LinearLayout listArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        ImageView backButtom = (ImageView) findViewById(R.id.videoPageBackButt);
        backButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoPlayActivity.this.finish();
            }
        });

        listArea = (LinearLayout) findViewById(R.id.otherVideoLIstArea);

        ImageView userImage = (ImageView) findViewById(R.id.videoUploaderImage);
        RoundView.setRadiusWithDp(32, userImage);

    }

    @Override
    protected void onStart() {
        super.onStart();
        addMuseumVideo("北京博物馆介绍", "essay", "13:14", "2021-05-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
        addMuseumVideo("Introduce", "末老师", "13:14", "2021-05-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
        addMuseumVideo("北京博物馆介绍", "essay", "13:14", "2021-05-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
        addMuseumVideo("故宫博物馆介绍", "czx", "13:14", "2021-06-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
        addMuseumVideo("国家博物馆介绍——某藏品介绍", "bleafumb", "13:14", "2021-05-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
        addMuseumVideo("Introduce of Beijing Museum", "末老师", "2:13:14", "2021-05-11 20:57", "https://youimg1.c-ctrip.com/target/100k0q000000gqnh8EE78_C_500_280_Q80.jpg");
    }

    private void addMuseumVideo(String title, String user, String time, String uploadTime, String imageSrc) {
        VideoListItem item = new VideoListItem(this);
        listArea.addView(item);
        RoundView.setRadiusWithDp(12, item);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) item.getLayoutParams();
        lp.setMargins(0, 0, 0, 32);
        item.setLayoutParams(lp);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initVideoPage(item);
            }
        });
        item.setAttr(title, user, time, uploadTime, imageSrc);
    }

    private void initVideoPage(VideoListItem item) {

    }
    private void loadVideoInfo(int id) {

    }
}
