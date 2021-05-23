package cn.edu.buct.se.cs1808;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.utils.DensityUtil;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.RoundView;
import cn.edu.buct.se.cs1808.utils.User;

public class UserInfoActivity extends AppCompatActivity {
    private ImageView userImage;
    private TextView userName;
    private TextView userEmail;
    private TextView userPhone;
    private LinearLayout gotoChangeUserAvatorPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userImage = (ImageView) findViewById(R.id.userInfoImage);
        RoundView.setRadiusWithDp(28, userImage);

        ImageView backButt = (ImageView) findViewById(R.id.userInfoBackButton);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.this.finish();
            }
        });

        userName = (TextView) findViewById(R.id.userInfoName);
        userEmail = (TextView) findViewById(R.id.userInfoEmail);
        userPhone = (TextView) findViewById(R.id.userInfoPhone);

        Button changeInfoButton = (Button) findViewById(R.id.changeUserInfoButton);
        changeInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, ChangeUserInfoActivity.class);
                startActivity(intent);
            }
        });

        gotoChangeUserAvatorPage = findViewById(R.id.openChangeAvatarPage);
        gotoChangeUserAvatorPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, ChangeUserAvatarActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        initUI();
    }

    /**
     * 根据登录的用户的信息初始化UI界面的文字
     */
    private void initUI() {
        JSONObject userInfo = User.getUserInfo(this);
        if (userInfo == null) {
            initDefaultUI();
            return;
        }
        try {
            String name = userInfo.getString("user_Name");
            String email = userInfo.getString("user_Email");
            String phone = userInfo.getString("user_Phone");
            String imageSrc = userInfo.getString("user_Avatar");
            userName.setText(name);
            userEmail.setText(email);
            userPhone.setText(phone);
            LoadImage loader = new LoadImage(userImage);
            loader.setBitmap(ApiTool.getADDRESS() + imageSrc);
        }
        catch (JSONException e) {
            initDefaultUI();
        }
    }

    /**
     * 在读取用户信息失败的时候初始化默认的UI
     */
    private void initDefaultUI() {
        userName.setText("游客");
        userEmail.setText("user@mail.example.com");
        userPhone.setText("1234567890");
    }
}
