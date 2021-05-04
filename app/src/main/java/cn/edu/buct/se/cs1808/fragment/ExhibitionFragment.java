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

import cn.edu.buct.se.cs1808.DetailsExhibitionActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;
import cn.edu.buct.se.cs1808.components.ExhibitionCard;

public class ExhibitionFragment extends NavBaseFragment{
    private LinearLayout exhibitionContainer;
    public ExhibitionFragment(){
        activityId = R.layout.activity_exhibition;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        exhibitionContainer = (LinearLayout)view.findViewById(R.id.main_exhibition_container);
        addExhibitionBox(10);
    }
    private void addExhibitionBox(int num){
        int defaultImage = R.drawable.bblk_museum_exhibition;
        String defauleName = "测试博物馆";
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
                    openDetailsExhibitionActivity(ctx);
                }
            });
            exhibitionContainer.addView(exhibitionCard);
        }
    }
    public static void openDetailsExhibitionActivity(Context context) {
        //页面跳转
        Intent intent = new Intent(context, DetailsExhibitionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
    @Override
    public int getItemId() {
        return R.id.navigation_home;
    }
}
