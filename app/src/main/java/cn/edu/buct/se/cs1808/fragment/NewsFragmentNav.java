package cn.edu.buct.se.cs1808.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.edu.buct.se.cs1808.DetailsExhibitionActivity;
import cn.edu.buct.se.cs1808.NewWebActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.components.NewsCard;

public class NewsFragmentNav extends NavBaseFragment {
    private View view;
    private LinearLayout newsTitleContainer;
    public NewsFragmentNav() {
        activityId = R.layout.activity_news;
    }

    @Override
    public int getItemId() {
        return R.id.navigation_news;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        newsTitleContainer=(LinearLayout) view.findViewById(R.id.news_container);
        addNewsTitleBox(10);

//        myWebView = (WebView) findViewById(R.id.myWebView);
//
//        myWebView.loadUrl("http://www.baidu.com/");
//        myWebView.setWebViewClient(new WebViewClient());

        //initUI();

    }
    private void addNewsTitleBox(int num)
    {
        int defauleImage= R.drawable.news_bgr;
        String defauleName ="“五一”假期小长假：敦煌博物馆迎来游客参观高峰";
        String name;
        int image;
        image=defauleImage;
        name=defauleName;



        for(int i=0;i<num;i++)
        {
            NewsCard newsCard=new NewsCard(ctx);
            //设置属性
            newsCard.setTitle(image,name);


            //获取自定义类内元素绑定事件
            TextView nName=newsCard.getNewsTitle();
            nName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNewWebActivity(ctx);
                }
            });
            newsTitleContainer.addView(newsCard);

        }
    }

    public static void openNewWebActivity(Context context) {
        //页面跳转
        Intent intent = new Intent(context, NewWebActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

//    private void initUI()
//    {
//        view.findViewById(R.id.webnew).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent();
//                intent.setClass(ctx,NewWebActivity.class);
//                startActivity(intent);
//            }
//        });

 //   }



}
