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
                if (selectedImagePath == null) return;
                uploadImage(selectedImagePath, userId);
            }
        });
    }

    private int userId;
    @Override
    protected void onResume() {
        super.onResume();
        JSONObject userInfo = User.getUserInfo(this);
        if (userInfo == null) {
            Toast.makeText(this, "未登录，请登录", Toast.LENGTH_SHORT).show();
            User.gotoLoginPage(this);
            return;
        }
        try {
            userId = userInfo.getInt("user_ID");
        }
        catch (JSONException e) {
            Toast.makeText(this, "未登录，请登录", Toast.LENGTH_SHORT).show();
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
        Uri uri = data.getData(); // 获取用户选择文件的URI
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
                        Toast.makeText(this, "请开启文件读权限!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * 上传文件
     * @param imagePath 图片路径
     * @param userId 用户ID
     */
    private void uploadImage(String imagePath, int userId) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_ID", String.valueOf(userId));
        }
        catch (JSONException e) {
            Toast.makeText(this, "上传失败，请稍后重试", Toast.LENGTH_SHORT).show();
            return;
        }

        FileEntity fileEntity = new FileEntity("file", imagePath, new File(imagePath), "image/png");
        ApiTool.request(this, ApiPath.CHANGE_USER_AVATAR, params, fileEntity, (JSONObject rep) -> {
            String code;
            try {
                code = rep.getString("code");
            }
            catch (JSONException e){
                code = "未知错误";
            }
            if (!"success".equals(code)) {
                Toast.makeText(this, "上传失败: " + code, Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show();
            // 应该更新用户的头像信息
            try {
                JSONObject info = rep.getJSONObject("info");
                String path = info.getString("path");
                if (!User.updateUserInfo(this, "user_Avatar", path)) {
                    // 更新失败，跳转到异常处理，提示用户重新登录
                    throw new JSONException("");
                }
            }
            catch (JSONException e) {
                // 可能是接口返回了异常的奇怪的数据，需要重新登录刷新头像信息
                Toast.makeText(this, "请重新登录刷新头像", Toast.LENGTH_SHORT).show();
                User.logout(this);
                User.gotoLoginPage(this);
            }
            finish();
        }, (JSONObject error) -> {
            Log.e("UploadError", error.toString());
            try {
                Toast.makeText(this, "请求失败: " + error.get("body"), Toast.LENGTH_SHORT).show();
            }
            catch (JSONException e) {
                Toast.makeText(this, "请求失败: 未知错误", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
