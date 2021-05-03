package cn.edu.buct.se.cs1808.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public abstract class NavBaseFragment extends Fragment {
    protected int activityId;
    protected Context ctx;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ctx = getContext();
        return inflater.inflate(activityId, container, false);
    }

    public int getActivityId() {
        return activityId;
    }

    abstract public int getItemId();

    protected Object findViewById(int id) {
        return getView().findViewById(id);
    }

}
