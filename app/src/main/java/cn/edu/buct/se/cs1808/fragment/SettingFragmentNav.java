package cn.edu.buct.se.cs1808.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import cn.edu.buct.se.cs1808.LoginPageActivity;
import cn.edu.buct.se.cs1808.MuseumActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.UserInfoActivity;
import cn.edu.buct.se.cs1808.utils.DensityUtil;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class SettingFragmentNav extends NavBaseFragment {
    private LinearLayout loginAndLogoutButton;
    private boolean isLogin = true;

    public SettingFragmentNav() {
        activityId = R.layout.activity_settings;
    }

    @Override
    public int getItemId() {
        return R.id.navigation_settings;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ConstraintLayout userCardLayout = (ConstraintLayout) findViewById(R.id.userInfoCard);
        RoundView.setRadiusWithDp(16, userCardLayout);
        userCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                if (!isLogin) {
                    // 未登录，跳转到登录页面
                    intent = new Intent(ctx,  LoginPageActivity.class);
                }
                else {
                    // 已经登陆, 跳转到个人资料
                    // 先临时跳转
                    intent = new Intent(ctx, UserInfoActivity.class);
                }
                startActivity(intent);
            }
        });

        ImageView userCardImage = (ImageView) findViewById(R.id.userCardImage);
        RoundView.setRadiusWithDp(32, userCardImage);
        loginAndLogoutButton = (LinearLayout) view.findViewById(R.id.mineLoginAndLogoutButton);

        initUI();
        initLoginAndLogoutButton();
    }
    private void initUI() {
        TextView text = loginAndLogoutButton.findViewById(R.id.loginButtonTitle);
        if (!isLogin) {
            text.setText("登录");
        }
        else {
            text.setText("退出登录");
        }
    }

    private void initLoginAndLogoutButton() {
        RoundView.setRadiusWithDp(8, loginAndLogoutButton);
        loginAndLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    // 退出登陆
                    isLogin = false;
                    initUI();
                }
                else {
                    // 跳到登陆页面
                    Intent intent = new Intent(ctx, LoginPageActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
