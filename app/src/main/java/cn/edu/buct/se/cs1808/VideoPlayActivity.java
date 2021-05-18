package cn.edu.buct.se.cs1808;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.VideoListItem;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class VideoPlayActivity extends AppCompatActivity {
    private LinearLayout listArea;
    private TextView videoTitle;
    private TextView userName;
    private TextView videoDescription;
    private TextView museName;
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

        videoTitle = (TextView) findViewById(R.id.videoTitle);
        userName = (TextView) findViewById(R.id.videoUploaderName);
        videoDescription = (TextView) findViewById(R.id.videoIntroduce);
        museName = (TextView) findViewById(R.id.videoMuseumName);

        initWithIntentParam(getIntent());

    }

    @Override
    protected void onStart() {
        super.onStart();
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
            }
        });
        item.setAttr(title, user, time, uploadTime, imageSrc);
    }

    /**
     * 更新UI，播放视频
     * @param title 视频标题
     * @param userName 视频上传者用户名
     * @param description 视频描述
     * @param time 视频时长
     * @param uploadTime 视频上传时间
     * @param museName 视频所属博物馆名称
     */
    private void initVideoPage(String title, String userName, String description, String time, String uploadTime, String museName) {
        videoTitle.setText(title);
        this.userName.setText(userName);
        videoDescription.setText(description);
        this.museName.setText(museName);
    }

    /**
     * 通过传递给该页面的参数初始化页面
     * @param intent 包含参数的intent
     */
    private void initWithIntentParam(Intent intent) {
        int id = intent.getIntExtra("video_ID", -1);
        if (id == -1) {
            Toast.makeText(this, "无视频参数", Toast.LENGTH_SHORT).show();
            return;
        }
        loadVideoInfo(id);
    }

    /**
     * 通过视频ID加载视频
     * @param id 视频ID
     */
    private void loadVideoInfo(int id) {
        JSONObject params = new JSONObject();
        try {
            // 由于目前接口只能查询一定范围内的
            // 所以需要查询一个大范围，之后再通过ID进行筛选
            params.put("pageSize", 100086);
            params.put("pageIndex", 1);
//            params.put("video_ID", id);
        }
        catch (JSONException e) {
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiTool.request(this, ApiPath.GET_VIDEO, params, (JSONObject rep) -> {
            String code;
            try {
                code = rep.getString("code");
            }
            catch (JSONException e){
                code = "未知错误";
            }
            if (!"success".equals(code)) {
                Toast.makeText(this, "加载失败: " + code, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                if (items.length() == 0) {
                    Toast.makeText(this, "无视频数据", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < items.length(); i ++) {
                    JSONObject item = items.getJSONObject(i);
                    int videoId = item.getInt("video_ID");
                    // 判断是否符合需要的videoID
                    if (videoId != id) continue;
                    String title = item.getString("video_Name");
                    String uploadTime = item.getString("video_Time");
                    // 暂时无法获取视频的时长
                    String time = "未知";
                    String userName = item.getString("user_Name");
                    String description = item.getString("video_Description");
                    String museName = item.getString("muse_Name");
                    // 还需要设置视频封面
                    // 以及视频对应的博物馆ID，名称，用户名称
                    initVideoPage(title, userName, description, time, uploadTime, museName);
                    break;
                }
            }
            catch (JSONException ignore) {
                Toast.makeText(this, "加载失败: " + code, Toast.LENGTH_SHORT).show();
            }
        }, (JSONObject error) -> {
            try {
                Toast.makeText(this, "请求失败: " + error.get("info"), Toast.LENGTH_SHORT).show();
            }
            catch (JSONException e) {
                Toast.makeText(this, "请求失败: 未知错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
