package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.jar.Attributes;

import cn.edu.buct.se.cs1808.R;


public class NewsCard extends LinearLayout {
    private ImageView newsImage;
    private TextView newsTitle;
    private View rootView;
    public NewsCard(Context context){
        super(context);
        rootView= LayoutInflater.from(context).inflate(R.layout.layout_news_detail, this,true);
    }

    public NewsCard(Context context, AttributeSet attrs){
        super(context,attrs);
        rootView=LayoutInflater.from(context).inflate(R.layout.layout_news_detail, this,true);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.NewsCard);
        if(typedArray!=null){
            int image = typedArray.getResourceId(R.styleable.NewsCard_news_image,0);
            String name = typedArray.getString(R.styleable.NewsCard_news_title);
            setTitle(image,name);
        }
    }

    public void setTitle(int image,String title)
    {
        newsImage=(ImageView)rootView.findViewById(R.id.news_detail_image);
        newsTitle=(TextView)rootView.findViewById(R.id.news_title_text);
        newsTitle.setText(title);
        newsImage.setImageResource(image);
    }

    public TextView getNewsTitle()
    {
        newsTitle=(TextView) rootView.findViewById(R.id.news_title_text);
        return newsTitle;
    }
}
