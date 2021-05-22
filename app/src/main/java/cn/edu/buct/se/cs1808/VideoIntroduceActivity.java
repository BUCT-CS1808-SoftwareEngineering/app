package cn.edu.buct.se.cs1808;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.VideoListItem;
import cn.edu.buct.se.cs1808.utils.RoundView;
import cn.edu.buct.se.cs1808.utils.VideoUtil;

public class VideoIntroduceActivity extends AppCompatActivity {
    private LinearLayout listArea;
    private EditText searchInput;

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
        searchInput = (EditText) findViewById(R.id.videoSearchInput);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = searchInput.getText().toString();
                search(q);
            }
        });

        initWithIntentParam(getIntent());
    }

    /**
     * 搜索讲解视频
     * @param q 搜索关键字
     */
    private void search(String q) {
        Log.i("Search", q);
        listArea.removeAllViews();
        loadVideoListByName(q, 1, 300);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 添加一个视频列表
     * @param title 视频标题
     * @param user 视频用户
     * @param time 视频时长
     * @param uploadTime 视频上传时间
     * @param imageSrc 视频封面图
     * @param videoId 视频ID
     * @param museName 博物馆名称
     */
    private VideoListItem addItem(String title, String user, String time, String uploadTime, String imageSrc, int videoId, String museName) {
        VideoListItem item = new VideoListItem(this);
        listArea.addView(item);
        RoundView.setRadiusWithDp(12, item);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) item.getLayoutParams();
        lp.setMargins(0, 0, 0, 32);
        item.setLayoutParams(lp);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoIntroduceActivity.this, VideoPlayActivity.class);
                intent.putExtra("video_ID", videoId);
                startActivity(intent);
            }
        });
        item.setAttr(title, user, time, uploadTime, imageSrc, museName);
        return item;
    }

    /**
     * 通过传给该界面的参数进行页面初始化
     * @param intent 包含参数的intent
     */
    private void initWithIntentParam(Intent intent) {
        int id = intent.getIntExtra("muse_ID", -1);
        String muse_Name = intent.getStringExtra("muse_Name");
        if (id == -1) {
            // id 丢失，通过name搜索
            setSearchWord(muse_Name);
            loadVideoListByName(muse_Name, 1, 300);
        }
        else {
            // 暂时页面没有做分页加载，因此先加载300条数据
            loadVideoListById(id, 1, 300);
        }
    }

    /**
     * 通过博物馆ID加载视频列表
     * @param id 博物馆ID
     */
    private void loadVideoListById(int id, int page, int size) {
        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", size);
            params.put("pageIndex", page);
            params.put("muse_ID", id);
        }
        catch (JSONException e) {
            Toast.makeText(this, "视频列表加载失败", Toast.LENGTH_SHORT).show();
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
                int showed = 0;
                for (int i = 0; i < items.length(); i ++) {
                    JSONObject item = items.getJSONObject(i);
                    int ifShow = item.getInt("video_IfShow");
                    // 未审核通过
                    if (ifShow == 0) continue;
                    String title = item.getString("video_Name");
                    String uploadTime = item.getString("video_Time");
                    int videoId = item.getInt("video_ID");
                    String userName = item.getString("user_Name");
                    String videoUrl = item.getString("video_Url");
                    // 视频封面与video路径名称对应
                    String imageUrl = getVideoImage(videoUrl);
                    String museName = item.getString("muse_Name");
                    // 暂时无法获取视频的时长
                    String time = "loading";
                    VideoListItem video = addItem(title, userName, time, uploadTime, imageUrl, videoId, museName);
                    VideoUtil.setVideoDuration(this, ApiTool.getADDRESS() + videoUrl, (int duration) -> {
                        video.setTime(VideoUtil.durationSecToString(duration));
                    });
                    showed ++;
                }
                if (showed == 0) {
                    Toast.makeText(this, "无视频数据", Toast.LENGTH_SHORT).show();
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

    /**
     * 通过博物馆名称搜索视频列表
     * @param name 博物馆名称，若名称为null，相当于搜索所有的博物馆的讲解视频
     */
    private void loadVideoListByName(String name, int page, int size) {
        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", size);
            params.put("pageIndex", page);
            if (name != null && name.length() > 0)
                params.put("muse_Name", name);
        }
        catch (JSONException e) {
            Toast.makeText(this, "视频列表加载失败", Toast.LENGTH_SHORT).show();
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
                int showed = 0;
                for (int i = 0; i < items.length(); i ++) {
                    JSONObject item = items.getJSONObject(i);
                    int ifShow = item.getInt("video_IfShow");
                    // 未审核通过
                    if (ifShow == 0) continue;
                    String title = item.getString("video_Name");
                    String uploadTime = item.getString("video_Time");
                    int videoId = item.getInt("video_ID");
                    String userName = item.getString("user_Name");
                    String videoUrl = item.getString("video_Url");
                    // 视频封面与video路径名称对应
                    String imageUrl = getVideoImage(videoUrl);
                    String museName = item.getString("muse_Name");
                    // 暂时无法获取视频的时长,在后面回调函数再次获取
                    String time = "loading";
                    VideoListItem video = addItem(title, userName, time, uploadTime, imageUrl, videoId, museName);
                    VideoUtil.setVideoDuration(this, ApiTool.getADDRESS() + videoUrl, (int duration) -> {
                        video.setTime(VideoUtil.durationSecToString(duration));
                    });
                    showed ++;
                }
                if (showed == 0) {
                    Toast.makeText(this, "无视频数据", Toast.LENGTH_SHORT).show();
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

    /**
     * 设置搜索框内的文字内容
     * @param word
     */
    private void setSearchWord(String word) {
        searchInput.setText(word);
    }

    /**
     * 获得视频路径对应的视频封面
     * @param path 视频路径，是相对路径
     */
    public static String getVideoImage(String path) {
        String res = path.replace("/upload", "/images");
        return ApiTool.getADDRESS() + res + "_1.jpg";
    }
}
