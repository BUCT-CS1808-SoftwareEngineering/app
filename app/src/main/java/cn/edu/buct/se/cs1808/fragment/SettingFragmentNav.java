package cn.edu.buct.se.cs1808.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.utils.DensityUtil;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class SettingFragmentNav extends NavBaseFragment {
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
        ImageView userCardImage = (ImageView) findViewById(R.id.userCardImage);
        RoundView.setRadiusWithDp(32, userCardImage);
        LinearLayout loginAndLogoutButton = (LinearLayout) view.findViewById(R.id.mineLoginAndLogoutButton);
        RoundView.setRadiusWithDp(8, loginAndLogoutButton);
    }
}
