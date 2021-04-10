package cn.edu.buct.se.cs1808.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.edu.buct.se.cs1808.R;

public class SettingFragmentNav extends Fragment implements NavBaseFragment {
    private final int activityId = R.layout.activity_settings;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(activityId, container, false);
    }

    @Override
    public int getActivityId() {
        return activityId;
    }

    @Override
    public int getItemId() {
        return R.id.navigation_settings;
    }
}
