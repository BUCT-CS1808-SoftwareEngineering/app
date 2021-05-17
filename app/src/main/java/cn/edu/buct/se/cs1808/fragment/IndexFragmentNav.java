package cn.edu.buct.se.cs1808.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.LeaderboardActivity;
import cn.edu.buct.se.cs1808.MuseumActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;
import cn.edu.buct.se.cs1808.SearchActivity;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.BoxTest;
import cn.edu.buct.se.cs1808.components.MuseumCard;

public class IndexFragmentNav extends NavBaseFragment {
    private LinearLayout searchContainer;
    private LinearLayout museumContainer;
    private TextView textMore;
    private ImageView searchButton;
    private JSONArray museumInfoArrayScore;
    private JSONArray museumInfoArrayClick;
    private TextView textAdd;
    private int indexNum; 

    public IndexFragmentNav() {
        activityId = R.layout.activity_index;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchContainer = (LinearLayout) view.findViewById(R.id.main_search_container);
        museumContainer = (LinearLayout) view.findViewById(R.id.main_museum_container);
        textMore = (TextView) view.findViewById(R.id.main_text_more);
        searchButton = (ImageView) view.findViewById(R.id.main_search_button);
        textAdd = (TextView) view.findViewById(R.id.main_addmore);
        museumInfoArrayScore = new JSONArray();
        museumInfoArrayClick = new JSONArray();
        indexNum = 1;

        addSearchBox(10);
        addMuseumBox(10);
        TextView lookMore = view.findViewById(R.id.main_text_more);
        TextView lead = view.findViewById(R.id.main_text_lead);
        TextView hotLead = view.findViewById(R.id.main_hot_lead);
        TextView museumLead = view.findViewById(R.id.main_museum_lead);
        //设置字体加粗、颜色
        lookMore.setTextColor(android.graphics.Color.parseColor("#ee7712"));
        lead.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        hotLead.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        museumLead.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        textMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLeaderBoard(ctx);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSearchActivity(ctx);
            }
        });

        textAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMuseumBox(10);
            }
        });
    }

    public static int dip2px(Context context, double dpValue) {
        final double scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void addSearchBox(int num){


        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", num);
            params.put("pageIndex", 1);
        }
        catch (JSONException e) {

        }
        ApiTool.request(ctx, ApiPath.GET_MUSEUM_INFO, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");

                int pos[] = new int[1];
                for(int i=0;i<items.length();i++){
                    pos[0] = i;

                    JSONObject it = items.getJSONObject(i);


                    double score[] = new double[1];
                    JSONObject scoreParams = new JSONObject();
                    try{
                        scoreParams.put("muse_ID",it.getInt("muse_ID"));
                    }
                    catch (JSONException e){

                    }
                    ApiTool.request(ctx, ApiPath.GET_MUSEUM_SCORE, scoreParams, (JSONObject repScore) -> {
                        // 请求成功，rep为请求获得的数据对象
                        try{
                            JSONObject infoScore = repScore.getJSONObject("info");
                            JSONObject itemsScore = infoScore.getJSONObject("items");
                            double score1 = itemsScore.getDouble("env_Review");
                            double score2 = itemsScore.getDouble("exhibt_Review");
                            double score3 = itemsScore.getDouble("service_Review");
                            score[0] = (score1+score2+score3)/3;
                            try{
                                it.put("muse_Score",score[0]);
                                museumInfoArrayClick.put(museumInfoArrayClick.length(),it);
                            }
                            catch (JSONException e){

                            }
                        }
                        catch(JSONException e){
                            int max=1,min=0;
                            double ran2 = (double) (Math.random()*(max-min)+min);
                            score[0] = 4+ran2;
                            try{
                                it.put("muse_Score",score[0]);
                                museumInfoArrayClick.put(museumInfoArrayClick.length(),it);
                            }
                            catch (JSONException ee){

                            }
                        }
                        generateSearchBox(it,pos[0]);
                    }, (JSONObject error) -> {
                        // 请求失败
                        int max=1,min=0;
                        double ran2 = (double) (Math.random()*(max-min)+min);
                        score[0] = 4+ran2;
                        try{
                            it.put("muse_Score",score[0]);
                            museumInfoArrayClick.put(museumInfoArrayClick.length(),it);
                            ;
                        }
                        catch (JSONException e){

                        }
                        generateSearchBox(it,pos[0]);
                    });
                }
            }
            catch(JSONException e){

            }
        }, (JSONObject error) -> {
            // 请求失败

        });


    }
    private void addMuseumBox(int num){

        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", num);
            params.put("pageIndex", indexNum);
        }
        catch (JSONException e) {

        }
        ApiTool.request(ctx, ApiPath.GET_MUSEUM_INFO, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");

                for(int i=0;i<items.length();i++){
                    JSONObject it = items.getJSONObject(i);

                    double score[] = new double[1];
                    JSONObject scoreParams = new JSONObject();
                    try{
                        scoreParams.put("muse_ID",it.getInt("muse_ID"));
                    }
                    catch (JSONException e){

                    }
                    ApiTool.request(ctx, ApiPath.GET_MUSEUM_SCORE, scoreParams, (JSONObject repScore) -> {
                        // 请求成功，rep为请求获得的数据对象
                        try{
                            JSONObject infoScore = repScore.getJSONObject("info");
                            JSONObject itemsScore = infoScore.getJSONObject("items");
                            double score1 = itemsScore.getDouble("env_Review");
                            double score2 = itemsScore.getDouble("exhibt_Review");
                            double score3 = itemsScore.getDouble("service_Review");
                            score[0] = (score1+score2+score3)/3;
                            try{
                                it.put("muse_Score",score[0]);
                                museumInfoArrayScore.put(museumInfoArrayScore.length(),it);
                            }
                            catch (JSONException e){

                            }
                        }
                        catch(JSONException e){
                            int max=1,min=0;
                            double ran2 = (double) (Math.random()*(max-min)+min);
                            score[0] = 4+ran2;
                            try{
                                it.put("muse_Score",score[0]);
                                museumInfoArrayScore.put(museumInfoArrayScore.length(),it);
                            }
                            catch (JSONException ee){

                            }
                        }
                        generateMuseumBox(it);


                    }, (JSONObject error) -> {
                        // 请求失败
                        int max=1,min=0;
                        double ran2 = (double) (Math.random()*(max-min)+min);
                        score[0] = 4+ran2;
                        try{
                            it.put("muse_Score",score[0]);
                            museumInfoArrayScore.put(museumInfoArrayScore.length(),it);
                        }
                        catch (JSONException e){

                        }
                        generateMuseumBox(it);
                    });

                }
                indexNum+=1;
            }
            catch(JSONException e){

            }
        }, (JSONObject error) -> {
            // 请求失败

        });

    }


    private void generateSearchBox(JSONObject it,int pos){
        //加载失败时的默认值
        String defaultImage = "";
        String defaultName = "暂无数据";
        String defaultScore = "0.0";
        String defaultText = "暂无数据";
        try {
            int defaultGrade = 4;
            int grade;
            String image,name, score;

            image = it.getString("muse_Img");
            name = it.getString("muse_Name");
            score = String.format("%.1f",it.getDouble("muse_Score"));
            grade = defaultGrade;

            grade = (pos + 1);
            BoxTest searchBox = new BoxTest(ctx);
            searchBox.setAttr(image, name, grade, score);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dip2px(ctx, 120), dip2px(ctx, 150));
            lp.setMargins(0, 0, dip2px(ctx, 10), 0);
            searchBox.setLayoutParams(lp);

            searchBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMuseumActivity(ctx,searchBox);
                }
            });
            searchContainer.addView(searchBox);
        }
        catch (JSONException e){

        }
    }
    private void generateMuseumBox(JSONObject it){
        //加载失败时的默认值
        String defaultImage = "";
        String defaultName = "暂无数据";
        String defaultScore = "0.0";
        try{
            String image;
            String name,text,grade;
            image = it.getString("muse_Img");
            name = it.getString("muse_Name");
            text = it.getString("muse_Intro");
            grade = String.format("%.1f",it.getDouble("muse_Score"));

            //新建对象
            MuseumCard museumCard = new MuseumCard(ctx);
            //设置属性
            museumCard.setAttr(image,name,text,grade);
            RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            museumCard.setLayoutParams(lp);
            //获取自定义类内元素绑定事件
            RoundImageView rImage = museumCard.getMuseumImage();
            TextView mName = museumCard.getMuseumName();
            museumCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMuseumActivity(ctx,museumCard);
                }
            });
            museumContainer.addView(museumCard);
        }
        catch(JSONException e){

        }
    }
    public void openMuseumActivity(Context context,MuseumCard museumCard) {
        Intent intent = new Intent(context, MuseumActivity.class);

        TextView tv = museumCard.getMuseumName();
        String name = tv.getText().toString();
        try{
            for(int i=0;i<museumInfoArrayScore.length();i++){
                JSONObject it = museumInfoArrayScore.getJSONObject(i);
                if(it.getString("muse_Name")==name){
                    intent.putExtra("muse_ID",it.getInt("muse_ID"));
                    break;
                }
            }
        }
        catch(JSONException e){

        }

        //页面跳转
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
    public void openMuseumActivity(Context context,BoxTest boxTest) {
        Intent intent = new Intent(context, MuseumActivity.class);

        TextView tv = boxTest.getBoxName();
        String name = tv.getText().toString();
        try{
            for(int i=0;i<museumInfoArrayClick.length();i++){
                JSONObject it = museumInfoArrayClick.getJSONObject(i);
                if(it.getString("muse_Name")==name){
                    intent.putExtra("muse_ID",it.getInt("muse_ID"));
                    break;
                }
            }
        }
        catch(JSONException e){

        }

        //页面跳转
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
    public static void openLeaderBoard(Context context) {
        //页面跳转
        Intent intent = new Intent(context, LeaderboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
    public static void openSearchActivity(Context context) {
        //页面跳转
        Intent intent = new Intent(context, SearchActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
    @Override
    public int getItemId() {
        return R.id.navigation_home;
    }
}
