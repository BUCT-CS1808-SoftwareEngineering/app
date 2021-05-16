package cn.edu.buct.se.cs1808.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.edu.buct.se.cs1808.DetailsEducationActivity;
import cn.edu.buct.se.cs1808.DetailsExhibitionActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;
import cn.edu.buct.se.cs1808.components.ExhibitionCard;

public class EducationFragment extends NavBaseFragment{
    private LinearLayout educationContainer;
    public EducationFragment(){
        activityId = R.layout.activity_education;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        educationContainer = (LinearLayout)view.findViewById(R.id.main_education_container);
        addEducationBox(10);
    }
    private void addEducationBox(int num){
        int defaultImage = R.drawable.bblk_museum_exhibition;
        String defauleName = "伟大征程——庆祝中国共产党成立100周年特展";
        int image;
        String name;
        image = defaultImage;
        name = defauleName;
        for(int i=0;i<num;i++){
            ExhibitionCard exhibitionCard=new ExhibitionCard(ctx);
            //设置属性
            exhibitionCard.setAttr(image,name);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            exhibitionCard.setLayoutParams(params);
            //获取自定义类内元素绑定事件
            RoundImageView rImage = exhibitionCard.getMuseumImage();
            TextView mName = exhibitionCard.getMuseumName();
            rImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDetailsEducationActivity(ctx);
                }
            });
            educationContainer.addView(exhibitionCard);
        }
    }
    public static void openDetailsEducationActivity(Context context) {
        //页面跳转
        Intent intent = new Intent(context, DetailsEducationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
    @Override
    public int getItemId() {
        return R.id.navigation_home;
    }
}
