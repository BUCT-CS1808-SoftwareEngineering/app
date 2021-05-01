package cn.edu.buct.se.cs1808.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.edu.buct.se.cs1808.R;

public class ObjectFragment extends Fragment{
   private int activityId;
   public ObjectFragment(){
       activityId = R.layout.activity_object;
   }
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(activityId, container, false);
    }
}
