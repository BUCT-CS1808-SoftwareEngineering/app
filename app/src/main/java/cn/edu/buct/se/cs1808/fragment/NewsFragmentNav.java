package cn.edu.buct.se.cs1808.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.NewWebActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.BoxTest;
import cn.edu.buct.se.cs1808.components.NewsCard;

public class NewsFragmentNav extends NavBaseFragment {
    private View view;
    private LinearLayout newsTitleContainer;
    private Button button;
    private ScrollView scroll;
    private JSONArray newsArray;
    private int indexNum;
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

        indexNum = 1;
        button=(Button) view.findViewById(R.id.next);
        scroll=(ScrollView) view.findViewById(R.id.news_scroll);
        newsTitleContainer=(LinearLayout) view.findViewById(R.id.news_container);
        newsArray = new JSONArray();
        addNewsBox(10);

//        myWebView = (WebView) findViewById(R.id.myWebView);
//
//        myWebView.loadUrl("http://www.baidu.com/");
//        myWebView.setWebViewClient(new WebViewClient());

        //initUI();

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                addNewsBox(10);
                button.setVisibility(View.INVISIBLE);
            }
        });
        scroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction()){
                    case MotionEvent.ACTION_MOVE:{
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        //当文本的measureheight 等于scroll滚动的长度+scroll的height
                        if(scroll.getChildAt(0).getMeasuredHeight()<=scroll.getScrollY()+scroll.getHeight()){
                            button.setVisibility(View.VISIBLE);
                        }else{

                        }
                        break;
                    }
                }
                return false;
            }
        });

    }
    private void addNewsBox(int num){
        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", num);
            params.put("pageIndex", indexNum);
        }
        catch (JSONException e) {

        }
        ApiTool.request(ctx, ApiPath. GET_NEWS_INFO, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");

                for(int i=0;i<items.length();i++){
                    JSONObject it = items.getJSONObject(i);
                    JSONObject newsParams = new JSONObject();
                    try{
                        newsParams.put("news_ID",it.getInt("news_ID"));
                        newsParams.put("news_Name",it.getString("news_Name"));
                        newsArray.put(newsArray.length(),newsParams);
                        generateNewsTitleBox(newsParams);
                        //newsParams.has()
                    }
                    catch (JSONException e){

                    }

                }
                indexNum+=1;
            }
            catch(JSONException e){

            }
        }, (JSONObject error) -> {
            // 请求失败

        });

    }
    private void generateNewsTitleBox(JSONObject it)
    {
        String defauleName ="“五一”假期小长假：敦煌博物馆迎来游客参观高峰";
        String name;

        name=defauleName;


        try {
            name = it.getString("news_Name");
            NewsCard newsCard=new NewsCard(ctx);
            //设置属性
            newsCard.setTitle(name);
            RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, dip2px(ctx,20));
            newsCard.setLayoutParams(lp);

            //获取自定义类内元素绑定事件
            TextView nName=newsCard.getNewsTitle();
            nName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNewWebActivity(ctx,newsCard);
                }
            });
            newsTitleContainer.addView(newsCard);
        }
        catch (JSONException e){

        }
    }

    public void openNewWebActivity(Context context,NewsCard newsCard) {
        //页面跳转
        Intent intent = new Intent(context, NewWebActivity.class);
        TextView tv = newsCard.getNewsName();
        String name = tv.getText().toString();
        try{
            for(int i=0;i<newsArray.length();i++){
                JSONObject it = newsArray.getJSONObject(i);
                if(it.getString("news_Name")==name){
                    intent.putExtra("news_ID",it.getInt("news_ID"));
                    break;
                }
            }
        }
        catch(JSONException e){

        }
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
