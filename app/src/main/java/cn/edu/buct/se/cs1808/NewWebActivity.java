package cn.edu.buct.se.cs1808;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        //Log.e("777",url);
        myWebView = (WebView) findViewById(R.id.myWebView);

        myWebView.loadUrl(url);
        myWebView.setWebViewClient(new WebViewClient());
    }
}
