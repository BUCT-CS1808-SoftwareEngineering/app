package cn.edu.buct.se.cs1808.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;

public class MuseumDetailFragment extends Fragment{
    private int activityId;
    private ArrayList<RoundImageView> objectImageArray= new ArrayList<>();
    private ArrayList<TextView> objectNameArray= new ArrayList<>();
    private RoundImageView exhibitionImage;
    private TextView exhibitionName;
    public MuseumDetailFragment(){
        activityId = R.layout.activity_museum_detail;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(activityId,container,false);
    }

    @Override
    public void onViewCreated( @Nullable View view, Bundle savedInstanceState) {
        objectImageArray.add((RoundImageView) view.findViewById(R.id.museum_detail_objectimage1));
        objectImageArray.add((RoundImageView) view.findViewById(R.id.museum_detail_objectimage2));
        objectImageArray.add((RoundImageView) view.findViewById(R.id.museum_detail_objectimage3));
        objectImageArray.add((RoundImageView) view.findViewById(R.id.museum_detail_objectimage4));

        objectNameArray.add((TextView) view.findViewById(R.id.museum_detail_objectname1));
        objectNameArray.add((TextView) view.findViewById(R.id.museum_detail_objectname2));
        objectNameArray.add((TextView) view.findViewById(R.id.museum_detail_objectname3));
        objectNameArray.add((TextView) view.findViewById(R.id.museum_detail_objectname4));

        exhibitionImage = (RoundImageView) view.findViewById(R.id.museum_detail_exhibitionimage);
        exhibitionName = (TextView) view.findViewById(R.id.museum_detail_exhibitionname);
        setObject();
        setExhibition();

        ArrayList<TextView> textArray = new ArrayList<>();
        textArray.add((TextView) view.findViewById(R.id.museum_detail_graph));
        textArray.add((TextView) view.findViewById(R.id.museum_detail_objectmore));
        textArray.add((TextView) view.findViewById(R.id.museum_detail_exhibitionmore));
        for(int i=0;i<textArray.size();i++){
            TextView tv = textArray.get(i);
            tv.setTextColor(android.graphics.Color.parseColor("#ee7712"));
        }


    }
    private void setObject(){
        String defaultName = "测试藏品测试藏品测试藏品测试藏品";
        int defaultImage = R.drawable.bleafumb_object;
        int num = objectImageArray.size();
        for(int i=0;i<num;i++){
            RoundImageView oImage = objectImageArray.get(i);
            oImage.setImageResource(defaultImage);
            TextView oName = objectNameArray.get(i);
            oName.setText(defaultName);
        }
    }
    private void setExhibition(){
        String defaultName = "测试展览测试展览测试展览测试展览测试展览测试展览测试展览测试展览测试展览测试展览";
        int defaultImage = R.drawable.bleafumb_exhibition;
        exhibitionImage.setImageResource(defaultImage);
        exhibitionName.setText(defaultName);
    }
}
