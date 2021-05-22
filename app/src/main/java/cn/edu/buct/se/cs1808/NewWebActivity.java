package cn.edu.buct.se.cs1808;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewWebActivity extends AppCompatActivity {


    private WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_web);
        Intent intent = getIntent();
        int newsId = intent.getIntExtra("news_ID",1);
        String url = "http://imessay.cn:8000/index.html?newsID="+newsId;
        //String url = "http://www.ifeng.com/?_zbs_firefox_gg";
        //Log.e("777",url);
        myWebView = (WebView) findViewById(R.id.myWebView);

        WebSettings mWebSettings = myWebView.getSettings();
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);//设置js可以直接打开窗口，如window.open()，默认为false
        mWebSettings.setJavaScriptEnabled(true);//是否允许JavaScript脚本运行，默认为false。设置true时，会提醒可能造成XSS漏洞
        mWebSettings.setSupportZoom(false);//是否可以缩放，默认true
        mWebSettings.setBuiltInZoomControls(false);//是否显示缩放按钮，默认false
        mWebSettings.setUseWideViewPort(false);//设置此属性，可任意比例缩放。大视图模式
        mWebSettings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        mWebSettings.setAppCacheEnabled(false);//是否使用缓存
        mWebSettings.setDomStorageEnabled(false);//开启本地DOM存储
        mWebSettings.setLoadsImagesAutomatically(true); // 加载图片
        mWebSettings.setMediaPlaybackRequiresUserGesture(false);//播放音频，多媒体需要用户手动？设置为false为可自动播放
        myWebView.loadUrl(url);
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
            //当网页面加载失败时，会调用 这个方法，所以我们在这个方法中处理
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
// TODO Auto-generated method stub
                Log.e("777",errorCode+"");
                Log.e("888",description);
                Log.e("999",failingUrl);
            }
        });
    }
}
