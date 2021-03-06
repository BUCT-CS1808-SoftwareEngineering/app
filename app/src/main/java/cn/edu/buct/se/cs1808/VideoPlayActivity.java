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
     * ??????UI???????????????
     * @param title ????????????
     * @param userName ????????????????????????
     * @param description ????????????
     * @param time ????????????
     * @param uploadTime ??????????????????
     * @param museName ???????????????????????????
     * @param imageUrl ??????????????????
     * @param videoUrl ????????????
     * @param userImage ????????????
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
     * ????????????????????????????????????????????????
     * @param intent ???????????????intent
     */
    private void initWithIntentParam(Intent intent) {
        int id = intent.getIntExtra("video_ID", -1);
        if (id == -1) {
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        loadVideoInfo(id);
    }

    /**
     * ????????????ID????????????
     * @param id ??????ID
     */
    private void loadVideoInfo(int id) {
        JSONObject params = new JSONObject();
        try {
            // ????????????????????????????????????
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
                code = "????????????";
            }
            if (!"success".equals(code)) {
                Toast.makeText(this, "????????????: " + code, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                if (items.length() == 0) {
                    Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < items.length(); i ++) {
                    JSONObject item = items.getJSONObject(i);
                    int videoId = item.getInt("video_ID");
                    // ???????????????????????????videoID
                    if (videoId != id) continue;
                    String title = item.getString("video_Name");
                    String uploadTime = item.getString("video_Time");
                    // ?????????????????????????????????
                    String time = "??????";
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
                Toast.makeText(this, "????????????: " + code, Toast.LENGTH_SHORT).show();
            }
        }, (JSONObject error) -> {
            try {
                Toast.makeText(this, "????????????: " + error.get("info"), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(this, "????????????: ????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * ??????????????????????????????????????????
     * @param museumId ?????????ID
     * @param currentVideoId ???????????????ID???????????????
     */
    private void loadOtherVideos(int museumId, int currentVideoId) {
        JSONObject params = new JSONObject();
        try {
            // ????????????????????????????????????
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
                code = "????????????";
            }
            if (!"success".equals(code)) {
                Toast.makeText(this, "????????????: " + code, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                for (int i = 0; i < items.length(); i ++) {
                    JSONObject item = items.getJSONObject(i);
                    int ifShow = item.getInt("video_IfShow");
                    // ???????????????
                    if (ifShow == 0) continue;
                    int videoId = item.getInt("video_ID");
                    // ???????????????????????????
                    if (videoId == currentVideoId) continue;
                    String title = item.getString("video_Name");
                    String uploadTime = item.getString("video_Time");
                    String userName = item.getString("user_Name");
                    String videoUrl = item.getString("video_Url");
                    String imageUrl = VideoIntroduceActivity.getVideoImage(videoUrl);
                    int videoID = item.getInt("video_ID");
                    // ?????????????????????????????????
                    String time = "loading";
                    VideoListItem video = addMuseumVideo(title, userName, time, uploadTime, imageUrl, videoID);
                    VideoUtil.setVideoDuration(this, ApiTool.getADDRESS() + videoUrl, (int duration) -> {
                        video.setTime(VideoUtil.durationSecToString(duration));
                    });
                }
            }
            catch (JSONException ignore) {
                Toast.makeText(this, "????????????: " + code, Toast.LENGTH_SHORT).show();
            }
        }, (JSONObject error) -> {
            try {
                Toast.makeText(this, "????????????: " + error.get("info"), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(this, "????????????: ????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
