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
import cn.edu.buct.se.cs1808.components.VideoViewPlus;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.RoundView;
import cn.edu.buct.se.cs1808.utils.VideoUtil;

public class VideoPlayActivity extends AppCompatActivity {
    private LinearLayout listArea;
    private TextView videoTitle;
    private TextView userName;
    private TextView videoDescription;
    private TextView museName;
    private VideoViewPlus videoPlayer;
    private ImageView videoUploaderImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Secs1808);
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
        videoPlayer = (VideoViewPlus) findViewById(R.id.videoPageVideo);
        videoUploaderImage = (ImageView) findViewById(R.id.videoUploaderImage);

        initWithIntentParam(getIntent());

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private VideoListItem addMuseumVideo(String title, String user, String time, String uploadTime, String imageSrc, int videoid) {
        VideoListItem item = new VideoListItem(this);
        listArea.addView(item);
        RoundView.setRadiusWithDp(12, item);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) item.getLayoutParams();
        lp.setMargins(0, 0, 0, 32);
        item.setLayoutParams(lp);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoPlayActivity.this, VideoPlayActivity.class);
                intent.putExtra("video_ID", videoid);
                startActivity(intent);
                finish();
            }
        });
        item.setAttr(title, user, time, uploadTime, imageSrc);
        return item;
    }

    /**
     * 更新UI，播放视频
     * @param title 视频标题
     * @param userName 视频上传者用户名
     * @param description 视频描述
     * @param time 视频时长
     * @param uploadTime 视频上传时间
     * @param museName 视频所属博物馆名称
     * @param imageUrl 视频封面路径
     * @param videoUrl 视频路径
     * @param userImage 用户头像
     */
    private void initVideoPage(String title, String userName, String description, String time, String uploadTime, String museName, String imageUrl, String videoUrl, String userImage) {
        videoTitle.setText(title);
        this.userName.setText(userName);
        videoDescription.setText(description);
        this.museName.setText(museName);
        videoPlayer.play(ApiTool.getADDRESS() + videoUrl);
        videoPlayer.setImage(imageUrl);
        LoadImage loader = new LoadImage(videoUploaderImage, false);
        loader.setBitmap(userImage);
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
            // 后端接口分页参数是必须的
            params.put("pageSize", 1);
            params.put("pageIndex", 1);
            params.put("video_ID", id);
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
                    String videoUrl = item.getString("video_Url");
                    String imageUrl = VideoIntroduceActivity.getVideoImage(videoUrl);
                    String userAvatar = item.getString("user_Avatar");
                    int museId = item.getInt("muse_ID");
                    loadOtherVideos(museId, videoId);
                    initVideoPage(title, userName, description, time, uploadTime, museName, imageUrl, videoUrl, ApiTool.getADDRESS() + userAvatar);
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
            catch (Exception e) {
                Toast.makeText(this, "请求失败: 未知错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 加载该博物馆的其他的讲解视频
     * @param museumId 博物馆ID
     * @param currentVideoId 当前的视频ID，用来过滤
     */
    private void loadOtherVideos(int museumId, int currentVideoId) {
        JSONObject params = new JSONObject();
        try {
            // 后端接口分页参数是必须的
            params.put("pageSize", 300);
            params.put("pageIndex", 1);
            params.put("muse_ID", museumId);
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
                for (int i = 0; i < items.length(); i ++) {
                    JSONObject item = items.getJSONObject(i);
                    int ifShow = item.getInt("video_IfShow");
                    // 未审核通过
                    if (ifShow == 0) continue;
                    int videoId = item.getInt("video_ID");
                    // 正在播放的跳过显示
                    if (videoId == currentVideoId) continue;
                    String title = item.getString("video_Name");
                    String uploadTime = item.getString("video_Time");
                    String userName = item.getString("user_Name");
                    String videoUrl = item.getString("video_Url");
                    String imageUrl = VideoIntroduceActivity.getVideoImage(videoUrl);
                    int videoID = item.getInt("video_ID");
                    // 暂时无法获取视频的时长
                    String time = "loading";
                    VideoListItem video = addMuseumVideo(title, userName, time, uploadTime, imageUrl, videoID);
                    VideoUtil.setVideoDuration(this, ApiTool.getADDRESS() + videoUrl, (int duration) -> {
                        video.setTime(VideoUtil.durationSecToString(duration));
                    });
                }
            }
            catch (JSONException ignore) {
                Toast.makeText(this, "加载失败: " + code, Toast.LENGTH_SHORT).show();
            }
        }, (JSONObject error) -> {
            try {
                Toast.makeText(this, "请求失败: " + error.get("info"), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(this, "请求失败: 未知错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
