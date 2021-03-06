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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mingle.widget.LoadingView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Objects;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.utils.FileEntity;
import cn.edu.buct.se.cs1808.utils.Permission;
import cn.edu.buct.se.cs1808.utils.UriToFilePath;
import cn.edu.buct.se.cs1808.utils.User;

public class ChangeUserAvatarActivity extends AppCompatActivity {
    private LinearLayout selectImageArea;
    private ImageView selectedImage;
    private String selectedImagePath;
    private Button uploadedButton;
    private LoadingView loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Secs1808);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_avatar);

        ImageView backButton = (ImageView) findViewById(R.id.changeUserAvatarBackButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        selectImageArea = (LinearLayout) findViewById(R.id.selectNewImageButton);
        selectImageArea.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (!Permission.check(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Permission.request(ChangeUserAvatarActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    return;
                }
                selectFile();
            }
        });
        selectedImage = (ImageView) findViewById(R.id.selectedImage);
        uploadedButton = (Button) findViewById(R.id.uploadImaheButton);
        uploadedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImagePath == null) {
                    Toast.makeText(ChangeUserAvatarActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                    return;
                }
                uploadImage(selectedImagePath, userId);
            }
        });

        loadingView = (LoadingView) findViewById(R.id.loadingView1);
        loadingView.bringToFront();
    }

    private int userId;
    @Override
    protected void onResume() {
        super.onResume();
        JSONObject userInfo = User.getUserInfo(this);
        if (userInfo == null) {
            Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
            User.gotoLoginPage(this);
            return;
        }
        try {
            userId = userInfo.getInt("user_ID");
        }
        catch (JSONException e) {
            Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show();
            User.gotoLoginPage(this);
        }
    }

    private void selectFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        Uri uri = data.getData(); // ???????????????????????????URI
        String path = UriToFilePath.getPath(getApplicationContext(), uri);
        Log.i("Select", path);
        selectedImagePath = path;
        selectedImage.setImageURI(uri);
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
                        Toast.makeText(this, "????????????????????????!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    private void hideLoadingView(boolean isShow) {
        if (isShow) {
            uploadedButton.setVisibility(View.GONE);
            loadingView.setVisibility(View.VISIBLE);
        }
        else {
            uploadedButton.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
        }
    }
    /**
     * ????????????
     * @param imagePath ????????????
     * @param userId ??????ID
     */
    private void uploadImage(String imagePath, int userId) {
        hideLoadingView(true);
        JSONObject params = new JSONObject();
        try {
            params.put("user_ID", String.valueOf(userId));
        }
        catch (Exception e) {
            Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }

        FileEntity fileEntity = new FileEntity("file", imagePath, new File(imagePath), "image/png");
        ApiTool.request(this, ApiPath.CHANGE_USER_AVATAR, params, fileEntity, (JSONObject rep) -> {
            hideLoadingView(false);
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
            Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
            // ?????????????????????????????????
            try {
                JSONObject info = rep.getJSONObject("info");
                String path = info.getString("path");
                if (!User.updateUserInfo(this, "user_Avatar", path)) {
                    // ???????????????????????????????????????????????????????????????
                    throw new JSONException("");
                }
            }
            catch (JSONException e) {
                // ???????????????????????????????????????????????????????????????????????????????????????
                Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                User.logout(this);
                User.gotoLoginPage(this);
            }
            finish();
        }, (JSONObject error) -> {
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
