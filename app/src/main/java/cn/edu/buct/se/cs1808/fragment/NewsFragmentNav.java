package cn.edu.buct.se.cs1808.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.edu.buct.se.cs1808.NewWebActivity;
import cn.edu.buct.se.cs1808.R;

public class NewsFragmentNav extends NavBaseFragment {
    private View view;
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
//        myWebView = (WebView) findViewById(R.id.myWebView);
//
//        myWebView.loadUrl("http://www.baidu.com/");
//        myWebView.setWebViewClient(new WebViewClient());

        initUI();

        View b1 = view.findViewById(R.id.button1);//找到你要设透明背景的layout 的id
        b1.getBackground().setAlpha(0);//0~255透明度值

    }
    private void initUI()
    {
        view.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(ctx,NewWebActivity.class);
                startActivity(intent);
            }
        });

    }


    //button点击跳转
    public void button1(View view)
    {
    }


}
