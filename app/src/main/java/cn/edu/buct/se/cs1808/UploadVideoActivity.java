package cn.edu.buct.se.cs1808;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.mingle.widget.LoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.VideoViewPlus;
import cn.edu.buct.se.cs1808.utils.FileEntity;
import cn.edu.buct.se.cs1808.utils.RoundView;
import cn.edu.buct.se.cs1808.utils.User;
import cn.edu.buct.se.cs1808.utils.Validation;

public class UploadVideoActivity extends AppCompatActivity {
    private TextView publicButton;
    private ImageView backButton;
    private EditText introduceInput;
    private VideoViewPlus selectedVideo;
    private EditText videoTitleInput;
    private TextView selectedMuseumName;
    private TextView selectMuseumButt;
    private LoadingView loadingView;

    private OptionsPickerView<String> pvOptions;
    private List<String> museumList;
    private List<Integer> museumId;
    private int selectedMuseumIndex;
    private String selectedVideoPath;
    private int userId;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        setTheme(R.style.Theme_Secs1808);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        publicButton = (TextView) findViewById(R.id.publicButton);
        publicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (museumId == null) return;
                String title = videoTitleInput.getText().toString();
                String introduce = introduceInput.getText().toString();
                int museum_Id = museumId.get(selectedMuseumIndex);
                String message = null;
                if ((message = Validation.lengthBetween(introduce, 8, 1024, "??????????????????")) != null) {
                    Toast.makeText(UploadVideoActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((message = Validation.lengthBetween(title, 4, 48, "??????????????????")) != null) {
                    Toast.makeText(UploadVideoActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadVideo(title, introduce, selectedVideoPath, museum_Id, userId);
            }
        });
        backButton = (ImageView) findViewById(R.id.backButton);
        introduceInput = (EditText) findViewById(R.id.introduceInput);
        selectedVideo = (VideoViewPlus) findViewById(R.id.selectedVideo);
        selectedVideo.setVerticalHeight(400);
        videoTitleInput = (EditText) findViewById(R.id.videoTitleInput);
        selectedMuseumName = (TextView) findViewById(R.id.selectedMuseumName);
        selectMuseumButt = (TextView) findViewById(R.id.selectMuseumButt);
        loadingView = (LoadingView) findViewById(R.id.loadingView);
        loadingView.bringToFront();
        initRadius();

        Intent intent = getIntent();
        if (intent != null) {
            String path = intent.getStringExtra("path");
            selectedVideoPath = path;
            Uri uri = Uri.parse(path);
            try {
                selectedVideo.setVideoFirstImage(uri);
            }
            catch (IllegalArgumentException e) {
                Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
            }
            selectedVideo.play(uri);
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadVideoActivity.this.finish();
            }
        });

        selectMuseumButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pvOptions == null) return;
                pvOptions.show();
            }
        });
        museumList = new ArrayList<>();
        museumId = new ArrayList<>();
        loadMuseumList();
    }

    private void initRadius() {
        int radius = 12;
        RoundView.setRadiusWithDp(radius, publicButton);
        RoundView.setRadiusWithDp(radius, selectedVideo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ????????????????????????
        JSONObject userInfo = User.getUserInfo(this);
        if (userInfo == null) {
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
            User.gotoLoginPage(this);
            return;
        }
        try {
            userId = userInfo.getInt("user_ID");
        }
        catch (JSONException e) {
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
            User.gotoLoginPage(this);
        }
    }

    /**
     * ?????????????????????
     */
    private void loadMuseumList() {

        JSONObject params = new JSONObject();
        try {
            params.put("pageIndex", 1);
            params.put("pageSize", 300);
        }
        catch (JSONException e) {
            Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiTool.request(this, ApiPath.GET_ALL_MUSEUM_INFO, params, (JSONObject rep) -> {
            String code;
            try {
                code = rep.getString("code");
            }
            catch (JSONException e) {
                code = null;
            }
            // ????????????code = success ?????????????????????????????????
            if (!"success".equals(code)) {
                Toast.makeText(this, "????????????: " + code, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject info = rep.getJSONObject("info");
                JSONArray data = info.getJSONArray("items");
                if (data.length() == 0) {
                    Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < data.length(); i ++) {
                    JSONObject item = data.getJSONObject(i);
                    if (!item.has("muse_Name")) continue;
                    String name = item.getString("muse_Name");
                    int id = item.getInt("muse_ID");
                    museumList.add(name);
                    museumId.add(id);
                }
                pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        selectMuseum(options1);
                    }
                }).setSubmitColor(Color.WHITE).setCancelColor(Color.WHITE)
                        .setSubmitText("??????")
                        .setSelectOptions(0)
                        .setTitleText("???????????????")
                        .setTitleColor(Color.BLACK)
                        .build();
                pvOptions.setPicker(museumList);
                selectMuseum(0);
            }
            catch (JSONException e) {
                Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
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
     * ?????????????????????
     * @param id ?????????????????????
     */
    private void selectMuseum(int id) {
        if (museumList == null) return;
        if (id < 0 || id >= museumList.size()) return;
        selectedMuseumIndex = id;
        selectedMuseumName.setText(museumList.get(id));
    }

    /**
     * ??????????????????
     * @param title ????????????
     * @param introduce ????????????
     * @param videoPath ????????????
     * @param museId ?????????????????????ID
     * @param userId ???????????????ID
     */
    private void uploadVideo(String title, String introduce, String videoPath, int museId, int userId) {
        loadingView.setVisibility(View.VISIBLE);
        JSONObject params = new JSONObject();
        try {
            params.put("muse_ID", String.valueOf(museId));
            params.put("user_ID", String.valueOf(userId));
            params.put("video_Name", title);
            params.put("video_Description", introduce);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            params.put("video_Time", df.format(new Date()));
        }
        catch (JSONException e) {
            Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }

        FileEntity file = new FileEntity("file", videoPath, new File(videoPath), "video/mp4");
        ApiTool.request(this, ApiPath.UPLOAD_VIDEO, params, file, (JSONObject rep) -> {
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
            Toast.makeText(this, "???????????????????????????????????????????????????", Toast.LENGTH_SHORT).show();
            loadingView.setVisibility(View.GONE);
            finish();
        }, (JSONObject error) -> {
            loadingView.setVisibility(View.GONE);
            Log.e("UploadError", error.toString());
            try {
                Toast.makeText(this, "????????????: " + error.get("body"), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(this, "????????????: ????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
