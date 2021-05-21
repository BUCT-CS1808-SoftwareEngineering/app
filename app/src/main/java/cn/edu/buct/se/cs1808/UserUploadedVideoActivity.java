package cn.edu.buct.se.cs1808;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.VideoListItem;
import cn.edu.buct.se.cs1808.utils.Permission;
import cn.edu.buct.se.cs1808.utils.RoundView;
import cn.edu.buct.se.cs1808.utils.UriToFilePath;
import cn.edu.buct.se.cs1808.utils.User;

public class UserUploadedVideoActivity extends AppCompatActivity {
    private ImageView uploadButton;
    private LinearLayout listArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uploaded_video);

        uploadButton = (ImageView) findViewById(R.id.gotoMineUploadedButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!Permission.check(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Permission.request(UserUploadedVideoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    return;
                }
                selectFile();
            }
        });
        listArea = (LinearLayout) findViewById(R.id.mineUploadLIstLayout);

        ImageView minePageBackButton = (ImageView) findViewById(R.id.minePageBackButton);
        minePageBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserUploadedVideoActivity.this.finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        listArea.removeAllViews();
        JSONObject userInfo = User.getUserInfo(this);
        if (userInfo != null) {
            try {
                int userId = userInfo.getInt("user_ID");
                loadUserVideo(userId, 300, 1);
            }
            catch (JSONException e) {
                Toast.makeText(this, "请重新登录", Toast.LENGTH_SHORT).show();
                User.gotoLoginPage(this);
                finish();
            }
        }
        else {
            Toast.makeText(this, "请重新登录", Toast.LENGTH_SHORT).show();
            User.gotoLoginPage(this);
            finish();
        }
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Uri uri = data.getData(); // 获取用户选择文件的URI
        String path = UriToFilePath.getPath(getApplicationContext(), uri);
        Log.i("Select", path);
        Intent intent = new Intent(this, UploadVideoActivity.class);
        intent.putExtra("path", path);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        if (requestCode == Permission.REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i ++) {
                if (Objects.equals(permissions[i], Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        selectFile();
                    }
                    else {
                        Toast.makeText(this, "请开启文件读权限!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * 添加视频卡片
     * @param title 视频标题
     * @param user 视频用户
     * @param time 视频时长
     * @param uploadTime 视频上传时间
     * @param imageSrc 视频封面图片地址
     * @param ifShow 视频状态
     * @param videoId 视频ID
     */
    private void addItem(String title, String user, String time, String uploadTime, String imageSrc, int ifShow, int videoId) {
        VideoListItem item = new VideoListItem(this);
        listArea.addView(item);
        RoundView.setRadiusWithDp(12, item);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) item.getLayoutParams();
        lp.setMargins(0, 0, 0, 32);
        item.setLayoutParams(lp);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserUploadedVideoActivity.this, VideoPlayActivity.class);
                intent.putExtra("video_ID", videoId);
                startActivity(intent);
            }
        });
        item.setAttr(title, user, time, uploadTime, imageSrc, ifShow != 0);
    }

    /**
     * 加载该用户的所有的讲解视频
     * @param userId 用户ID
     */
    private void loadUserVideo(int userId, int size, int page) {
        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", size);
            params.put("pageIndex", page);
            params.put("user_ID", userId);
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
                    String title = item.getString("video_Name");
                    String uploadTime = item.getString("video_Time");
                    int videoId = item.getInt("video_ID");
                    // 暂时无法获取视频的时长
                    String time = "未知";
                    String userName = item.getString("user_Name");
                    String videoUrl = item.getString("video_Url");
                    // 视频封面与video路径名称对应
                    String imageUrl = VideoIntroduceActivity.getVideoImage(videoUrl);
                    addItem(title, userName, time, uploadTime, imageUrl, ifShow, videoId);
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
}
