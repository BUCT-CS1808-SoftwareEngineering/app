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

import cn.edu.buct.se.cs1808.DetailsObjectActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;
import cn.edu.buct.se.cs1808.components.ObjectCard;

public class ObjectFragment extends NavBaseFragment{
    private LinearLayout objectContainer;
    public ObjectFragment(){
        activityId = R.layout.activity_object;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        objectContainer = (LinearLayout)view.findViewById(R.id.main_object_container);
        addObjectBox(10);
    }
    private void addObjectBox(int num){
        int defaultImage = R.drawable.bblk_object;
        String defauleName = "青花瓷";
        int image;
        String name;
        image = defaultImage;
        name = defauleName;
        for(int i=0;i<num;i++){
            ObjectCard objectCard=new ObjectCard(ctx);
            //设置属性
            objectCard.setAttr(image,name);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            objectCard.setLayoutParams(params);
            //获取自定义类内元素绑定事件
            RoundImageView rImage = objectCard.getMuseumImage();
            TextView mName = objectCard.getMuseumName();
            rImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openDetailsExhibitionActivity(ctx);
                }
            });
            objectContainer.addView(objectCard);
        }
    }
    public static void openDetailsExhibitionActivity(Context context) {
        //页面跳转
        Intent intent = new Intent(context, DetailsObjectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
    @Override
    public int getItemId() {
        return R.id.navigation_home;
    }
}

