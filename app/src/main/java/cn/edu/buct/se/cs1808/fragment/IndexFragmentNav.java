package cn.edu.buct.se.cs1808.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.edu.buct.se.cs1808.R;

public class IndexFragmentNav extends NavBaseFragment {
    public IndexFragmentNav() {
        activityId = R.layout.activity_index;
    }

    @Override
    public int getItemId() {
        return R.id.navigation_home;
    }
}
