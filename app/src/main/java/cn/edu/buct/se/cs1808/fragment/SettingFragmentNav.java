package cn.edu.buct.se.cs1808.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.edu.buct.se.cs1808.R;

public class SettingFragmentNav extends NavBaseFragment {
    public SettingFragmentNav() {
        activityId = R.layout.activity_settings;
    }

    @Override
    public int getItemId() {
        return R.id.navigation_settings;
    }
}
