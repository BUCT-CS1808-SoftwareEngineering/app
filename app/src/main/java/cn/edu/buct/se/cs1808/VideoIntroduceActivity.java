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
import cn.edu.buct.se.cs1808.utils.JSONArraySort;
import cn.edu.buct.se.cs1808.utils.RoundView;
import cn.edu.buct.se.cs1808.utils.VideoUtil;

public class VideoIntroduceActivity extends AppCompatActivity {
    private LinearLayout listArea;
    private EditText searchInput;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_Secs1808);
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
     * ??????????????????
     * @param q ???????????????
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
     * ????????????????????????
     * @param title ????????????
     * @param user ????????????
     * @param time ????????????
     * @param uploadTime ??????????????????
     * @param imageSrc ???????????????
     * @param videoId ??????ID
     * @param museName ???????????????
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
     * ???????????????????????????????????????????????????
     * @param intent ???????????????intent
     */
    private void initWithIntentParam(Intent intent) {
        int id = intent.getIntExtra("muse_ID", -1);
        String muse_Name = intent.getStringExtra("muse_Name");
        if (id == -1) {
            // id ???????????????name??????
            setSearchWord(muse_Name);
            loadVideoListByName(muse_Name, 1, 300);
        }
        else {
            // ???????????????????????????????????????????????????300?????????
            loadVideoListById(id, 1, 300);
        }
    }

    /**
     * ???????????????ID??????????????????
     * @param id ?????????ID
     */
    private void loadVideoListById(int id, int page, int size) {
        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", size);
            params.put("pageIndex", page);
            params.put("muse_ID", id);
        }
        catch (JSONException e) {
            Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
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
                int showed = 0;
                for (int i = 0; i < items.length(); i ++) {
                    JSONObject item = items.getJSONObject(i);
                    int ifShow = item.getInt("video_IfShow");
                    // ???????????????
                    if (ifShow == 0) continue;
                    String title = item.getString("video_Name");
                    String uploadTime = item.getString("video_Time");
                    int videoId = item.getInt("video_ID");
                    String userName = item.getString("user_Name");
                    String videoUrl = item.getString("video_Url");
                    // ???????????????video??????????????????
                    String imageUrl = getVideoImage(videoUrl);
                    String museName = item.getString("muse_Name");
                    // ?????????????????????????????????
                    String time = "loading";
                    VideoListItem video = addItem(title, userName, time, uploadTime, imageUrl, videoId, museName);
                    VideoUtil.setVideoDuration(this, ApiTool.getADDRESS() + videoUrl, (int duration) -> {
                        video.setTime(VideoUtil.durationSecToString(duration));
                    });
                    showed ++;
                }
                if (showed == 0) {
                    Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
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
     * ???????????????????????????????????????
     * @param name ??????????????????????????????null???????????????????????????????????????????????????
     */
    private void loadVideoListByName(String name, int page, int size) {
        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", size);
            params.put("pageIndex", page);
            if (name != null && name.length() > 0) {
                // ??????????????????API????????????????????????????????????????????????????????????????????????????????????????????????
                // ????????????????????????,??????????????????
                String newName = name.replaceAll("??????(???|???)?", "");
                if (newName.length() == 0) {
                    newName = name;
                }
                params.put("muse_Name", newName);
            }
        }
        catch (JSONException e) {
            Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
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
                if (name != null) {
                    items = JSONArraySort.sort(name, items);
                }
                int showed = 0;
                for (int i = 0; i < items.length(); i ++) {
                    JSONObject item = items.getJSONObject(i);
                    int ifShow = item.getInt("video_IfShow");
                    // ???????????????
                    if (ifShow == 0) continue;
                    String title = item.getString("video_Name");
                    String uploadTime = item.getString("video_Time");
                    int videoId = item.getInt("video_ID");
                    String userName = item.getString("user_Name");
                    String videoUrl = item.getString("video_Url");
                    // ???????????????video??????????????????
                    String imageUrl = getVideoImage(videoUrl);
                    String museName = item.getString("muse_Name");
                    // ?????????????????????????????????,?????????????????????????????????
                    String time = "loading";
                    VideoListItem video = addItem(title, userName, time, uploadTime, imageUrl, videoId, museName);
                    VideoUtil.setVideoDuration(this, ApiTool.getADDRESS() + videoUrl, (int duration) -> {
                        video.setTime(VideoUtil.durationSecToString(duration));
                    });
                    showed ++;
                }
                if (showed == 0) {
                    Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
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
     * ?????????????????????????????????
     * @param word
     */
    private void setSearchWord(String word) {
        searchInput.setText(word);
    }

    /**
     * ???????????????????????????????????????
     * @param path ??????????????????????????????
     */
    public static String getVideoImage(String path) {
        String res = path.replace("/upload", "/images");
        return ApiTool.getADDRESS() + res + "_1.jpg";
    }
}
