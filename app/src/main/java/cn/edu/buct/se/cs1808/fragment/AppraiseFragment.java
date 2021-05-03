package cn.edu.buct.se.cs1808.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;
import cn.edu.buct.se.cs1808.components.AppraiseCard;
import cn.edu.buct.se.cs1808.components.AppraiseScore;
import cn.edu.buct.se.cs1808.components.BoxTest;

public class AppraiseFragment extends Fragment{
    private int activityId;
    private Context ctx;
    private AppraiseScore myScore;
    private ArrayList<TextView> buttonArray;
    private TextView appraiseNumber;
    private LinearLayout appraiseContainer;

    public AppraiseFragment(){
        activityId = R.layout.activity_appraise;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ctx = getContext();
        return inflater.inflate(activityId, container, false);
    }
    @Override
    public void onViewCreated( @Nullable View view, Bundle savedInstanceState) {
        //元素获取
        buttonArray = new ArrayList<>();
        buttonArray.add((TextView) view.findViewById(R.id.appraise_activity_allappraise));
        buttonArray.add((TextView) view.findViewById(R.id.appraise_activity_goodappraise));
        buttonArray.add((TextView) view.findViewById(R.id.appraise_activity_midappraise));
        buttonArray.add((TextView) view.findViewById(R.id.appraise_activity_badappraise));
        myScore = (AppraiseScore) view.findViewById(R.id.appraise_activity_myscore);
        appraiseNumber = (TextView) view.findViewById(R.id.appraise_activity_number);
        appraiseContainer = (LinearLayout) view.findViewById(R.id.appraise_activity_container);

        appraiseModeChange((TextView) view.findViewById(R.id.appraise_activity_allappraise));

        //评价切换事件绑定
        for(int i=0;i<buttonArray.size();i++){
            TextView tv = buttonArray.get(i);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appraiseModeChange(tv);
                }
            });
        }
    }
    //dp转px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    //评价类型切换
    private void appraiseModeChange(TextView ele){
        appraiseContainer.removeAllViews();
        for(int i=0;i<buttonArray.size();i++){
            TextView tv = buttonArray.get(i);
            if(tv==ele){
                tv.setBackgroundResource(R.drawable.common_orange_background);
                tv.setTextColor(android.graphics.Color.parseColor("#ffffff"));
            }
            else{
                tv.setBackgroundResource(R.color.white);
                tv.setTextColor(android.graphics.Color.parseColor("#000000"));
            }
        }
        addAppraiseCard(10);
    }
    //设置评价条数
    private void setAppraiseNumber(int num){
        appraiseNumber.setText(num+"条");
    }
    //添加评价记录
    private void addAppraiseCard(int num){
        int defaultImage = R.drawable.bleafumb_object;
        String defauleName = "bleafumb";
        String defaultTime = "2021-4-30";
        String defaultComment = "文法G的所有有效项目集组成的集合，称为G的LR(0)项目集规范族。n重独立试验：若n个独立试验是相同的，则称其为n重独立试验。若每次试验的结果只有两个或，则称其为n重贝努利试验。";
        int defaultScore=4;
        int image,score;
        String name,comment,time;
        image = defaultImage;
        comment = defaultComment;
        name = defauleName;
        score = defaultScore;
        time = defaultTime;
        for(int i=0;i<num;i++) {
            AppraiseCard appraiseCard = new AppraiseCard(ctx);
            appraiseCard.setAttr(image, name, time, score, comment);
            RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, dip2px(ctx, 10), 0, dip2px(ctx, 10));
            appraiseCard.setLayoutParams(lp);
            appraiseContainer.addView(appraiseCard);
        }
    }
}
