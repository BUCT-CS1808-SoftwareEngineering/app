package cn.edu.buct.se.cs1808.fragment;

import android.content.Intent;
import android.media.Image;
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

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.LoginPageActivity;
import cn.edu.buct.se.cs1808.MuseumActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.UserInfoActivity;
import cn.edu.buct.se.cs1808.components.MinePageList;
import cn.edu.buct.se.cs1808.components.MinePageListItem;
import cn.edu.buct.se.cs1808.utils.DensityUtil;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.RoundView;
import cn.edu.buct.se.cs1808.utils.User;

public class SettingFragmentNav extends NavBaseFragment {
    private LinearLayout loginAndLogoutButton;
    private ImageView userCardImage;
    private TextView userCardName;
    private TextView userCardMail;

    /**
     * 在未登陆时候不应该显示的控件
     */
    private MinePageList userItemList;


    private JSONObject userInfo;
    private boolean isLogin = false;

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
                    intent = new Intent(ctx, UserInfoActivity.class);
                }
                startActivity(intent);
            }
        });
        userItemList = (MinePageList) findViewById(R.id.userItemList);


        userCardImage = (ImageView) findViewById(R.id.userCardImage);
        RoundView.setRadiusWithDp(32, userCardImage);
        loginAndLogoutButton = (LinearLayout) view.findViewById(R.id.mineLoginAndLogoutButton);
        RoundView.setRadiusWithDp(8, loginAndLogoutButton);
        userCardName = (TextView) view.findViewById(R.id.userCardName);
        userCardMail = (TextView) view.findViewById(R.id.userCardMail);


        initUI();
        initLoginAndLogoutButton();
    }

    /**
     * 根据登录状态刷新UI
     */
    private void initUI() {
        isLogin = ((userInfo = User.getUserInfo(ctx)) != null);
        TextView text = loginAndLogoutButton.findViewById(R.id.loginButtonTitle);
        if (!isLogin) {
            userItemList.setVisibility(View.GONE);
            text.setText("登录");
        }
        else {
            userItemList.setVisibility(View.VISIBLE);
            text.setText("退出登录");
        }
        if (userInfo == null) {
            initDefaultUI();
            return;
        }
        try {
            String name = userInfo.getString("user_Name");
            String mail = userInfo.getString("user_Email");
            String imageSrc = userInfo.getString("user_Avatar");
            userCardMail.setText(mail);
            userCardName.setText(name);
            LoadImage loader = new LoadImage(userCardImage);
            loader.setBitmap(imageSrc);
        }
        catch (JSONException ignore) {};
    }

    /**
     * 未登录情况下初始化默认的界面文字
     */
    private void initDefaultUI() {
        userCardMail.setText("user@mail.example.com");
        userCardName.setText("游客");
        userCardImage.setImageResource(R.drawable.essay_user_default);
    }

    /**
     * 初始化按钮点击事件
     */
    private void initLoginAndLogoutButton() {
        loginAndLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLogin) {
                    // 退出登陆
                    isLogin = false;
                    userInfo = null;
                    User.logout(ctx);
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
        initUI();;
    }

    @Override
    public void onStart() {
        super.onStart();
        initUI();;
    }
}
