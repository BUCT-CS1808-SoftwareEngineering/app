package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.jar.Attributes;

import cn.edu.buct.se.cs1808.R;


public class NewsCard extends LinearLayout {
    private TextView newsTitle;
    private View rootView;
    private LinearLayout newsBox;
    private Context ctx;
    public NewsCard(Context context){
        super(context);
        ctx = context;
        rootView= LayoutInflater.from(context).inflate(R.layout.layout_news_detail, this,true);
    }

    public NewsCard(Context context, AttributeSet attrs){
        super(context,attrs);
        rootView=LayoutInflater.from(context).inflate(R.layout.layout_news_detail, this,true);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.NewsCard);
        if(typedArray!=null){
            int image = typedArray.getResourceId(R.styleable.NewsCard_news_image,0);
            String name = typedArray.getString(R.styleable.NewsCard_news_title);
            setTitle(name);
        }
    }

    public void setTitle(String title)
    {
        newsTitle=(TextView)rootView.findViewById(R.id.news_title_text);
        newsBox=(LinearLayout) rootView.findViewById(R.id.news_title_box);
        newsTitle.setText(title);
    }

    public TextView getNewsTitle()
    {
        newsTitle=(TextView) rootView.findViewById(R.id.news_title_text);
        return newsTitle;
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public TextView getNewsName(){
        return newsTitle;
    }
}
