package cn.edu.buct.se.cs1808;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import cn.edu.buct.se.cs1808.utils.Permission;
import cn.edu.buct.se.cs1808.utils.UriToFilePath;

public class UserUploadedVideoActivity extends AppCompatActivity {
    private ImageView uploadButton;
    private LinearLayout listArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_uploaded_video);

        uploadButton = (ImageView) findViewById(R.id.uploadButton);
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
}
