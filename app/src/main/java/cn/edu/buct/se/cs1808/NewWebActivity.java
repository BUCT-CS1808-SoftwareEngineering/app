package cn.edu.buct.se.cs1808;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NewWebActivity extends AppCompatActivity {


    private WebView myWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_web);


        myWebView = (WebView) findViewById(R.id.myWebView);

        myWebView.loadUrl("http://www.baidu.com/");
        myWebView.setWebViewClient(new WebViewClient());
    }
}
