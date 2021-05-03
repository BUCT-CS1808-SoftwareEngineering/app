package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.edu.buct.se.cs1808.R;

public class AppraiseScore extends LinearLayout {
    private ArrayList<ImageView> starArray = new ArrayList<>();
    private View rootView;
    public AppraiseScore(Context context){
        super(context);
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_appraise_score, this,true);
        initArray();
        setScore(0);
    }
    public AppraiseScore(Context context, AttributeSet attrs){
        super(context,attrs);
        rootView=LayoutInflater.from(context).inflate(R.layout.layout_appraise_score, this,true);
        initArray();
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.AppraiseScore);
        if(typedArray!=null){
            int score = typedArray.getInt(R.styleable.AppraiseScore_appraise_score_number,3);
            setScore(score);
        }
    }
    public AppraiseScore(Context context,AttributeSet attrs,int defStyle){
        super(context,attrs,defStyle);
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_appraise_score, this,true);
        initArray();
        setScore(0);
    }
    private void initArray(){
        if(starArray.size()==0){
            starArray.add((ImageView) rootView.findViewById(R.id.appraise_score_star1));
            starArray.add((ImageView) rootView.findViewById(R.id.appraise_score_star2));
            starArray.add((ImageView) rootView.findViewById(R.id.appraise_score_star3));
            starArray.add((ImageView) rootView.findViewById(R.id.appraise_score_star4));
            starArray.add((ImageView) rootView.findViewById(R.id.appraise_score_star5));
        }
    }
    public void setScore(int score){
        if(starArray.size()==0){
            initArray();
        }
        if(score<0||score>5){
            return;
        }
        for(int i=0;i<score;i++){
            ImageView image = starArray.get(i);
            image.setImageResource(R.drawable.bleafumb_appraise_1);
        }
        for(int i=score;i<5;i++){
            ImageView image = starArray.get(i);
            image.setImageResource(R.drawable.bleafumb_appraise_2);
        }
    }
}
