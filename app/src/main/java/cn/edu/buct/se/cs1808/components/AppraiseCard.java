package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class AppraiseCard extends LinearLayout {
    private View rootView;
    private int textCutLen;
    private RelativeLayout userImageContainer;
    public AppraiseCard(Context context){
        super(context);
        rootView= LayoutInflater.from(context).inflate(R.layout.layout_appraise_card, this,true);
    }
    public AppraiseCard(Context context, AttributeSet attrs){
        super(context,attrs);
        rootView= LayoutInflater.from(context).inflate(R.layout.layout_appraise_card, this,true);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.AppraiseCard);
        if(typedArray!=null){
            int image = typedArray.getResourceId(R.styleable.AppraiseCard_appraise_image,0);
            String name = typedArray.getString(R.styleable.AppraiseCard_appraise_name);
            String time = typedArray.getString(R.styleable.AppraiseCard_appraise_time);
            String comment = typedArray.getString(R.styleable.AppraiseCard_appraise_comment);
            int score = typedArray.getInt(R.styleable.AppraiseCard_appraise_score,2);
            setAttr(image,name,time,score,comment);
        }
    }
    public void setAttr(int image,String name,String time,int score,String comment){
        textCutLen = 150;
        //设置最大评论长度
        int len = comment.length();
        if(len>=textCutLen){
            comment = comment.substring(0,textCutLen);
        }

        ImageView appraiseImage = (RoundImageView) rootView.findViewById(R.id.appraise_card_image);
        TextView appraiseName = (TextView) rootView.findViewById(R.id.appraise_card_name);
        TextView appraiseTime = (TextView) rootView.findViewById(R.id.appraise_card_time);
        TextView appraiseComment = (TextView) rootView.findViewById(R.id.appraise_card_comment);
        AppraiseScore appraiseScore = (AppraiseScore) rootView.findViewById(R.id.appraise_card_score);

        appraiseImage.setImageResource(image);
        appraiseName.setText(name);
        appraiseTime.setText(time);
        appraiseComment.setText(comment);
        appraiseScore.setScore(score);

        LinearLayout gbox= (LinearLayout) rootView.findViewById(R.id.appraise_card_greybox);
        RoundView.setRadius(24,gbox);
        setImage();
    }
    public void setAttr(String image,String name,String time,int score,String comment){
        textCutLen = 150;
        //设置最大评论长度
        int len = comment.length();
        if(len>=textCutLen){
            comment = comment.substring(0,textCutLen);
        }

        ImageView appraiseImage = (RoundImageView) rootView.findViewById(R.id.appraise_card_image);
        TextView appraiseName = (TextView) rootView.findViewById(R.id.appraise_card_name);
        TextView appraiseTime = (TextView) rootView.findViewById(R.id.appraise_card_time);
        TextView appraiseComment = (TextView) rootView.findViewById(R.id.appraise_card_comment);
        AppraiseScore appraiseScore = (AppraiseScore) rootView.findViewById(R.id.appraise_card_score);

        LoadImage loader = new LoadImage(appraiseImage, false);
        loader.setBitmap(image);
        appraiseName.setText(name);
        appraiseTime.setText(time);
        appraiseComment.setText(comment);
        appraiseScore.setScore(score);

        LinearLayout gbox= (LinearLayout) rootView.findViewById(R.id.appraise_card_greybox);
        RoundView.setRadius(24,gbox);
        setImage();
    }
    private void setImage(){
        userImageContainer = rootView.findViewById(R.id.appraise_user_image);
        RoundView.setRadiusWithDp(30, userImageContainer);
    }
}
