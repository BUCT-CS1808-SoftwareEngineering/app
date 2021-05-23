package cn.edu.buct.se.cs1808.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import cn.edu.buct.se.cs1808.LeaderboardActivity;
import cn.edu.buct.se.cs1808.MuseumActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.components.RoundImageView;
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
    private JSONArray museumInfoArrayClick;
    private JSONArray museumArray;
    private JSONArray museumShowArray;

    private HashMap<Integer,Integer> idMap;
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
        museumInfoArrayClick = new JSONArray();
        museumArray = new JSONArray();
        museumShowArray = new JSONArray();

        idMap = new HashMap<>();
        indexNum = 1;
        getMuseumArray(false);
        addSearchBox(5);

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
                addMuseumBox(10,true);
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
            params.put("muse_Name", ".");
        }
        catch (JSONException e) {

        }
        ApiTool.request(ctx, ApiPath.GET_MUSEUM_BY_CLICK, params, (JSONObject rep) -> {
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
                            double score1 = infoScore.getDouble("env_Review");
                            double score2 = infoScore.getDouble("exhibt_Review");
                            double score3 = infoScore.getDouble("service_Review");
                            score[0] = (score1+score2+score3)/3;
                            try{
                                it.put("muse_Score",score[0]);
                                museumInfoArrayClick.put(museumInfoArrayClick.length(),it);
                            }
                            catch (JSONException e){

                            }
                        }
                        catch(JSONException e){
                            score[0] = -1;
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
                        score[0] = -1;
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
    private void addMuseumBox(int num,boolean tip){
        //num indexNum
        int sum[] = new int[1];

        int index[] = new int[1];
        
        int end = Math.min(indexNum*num,museumArray.length());
        sum[0]=(indexNum-1)*num;
        for(int pos = (indexNum-1)*num;pos<end;pos++){
            index[0]=pos;
            try{
                JSONObject it = museumArray.getJSONObject(pos);
                it.put("card_Pos",index[0]);
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
                        double score1 = infoScore.getDouble("env_Review");
                        double score2 = infoScore.getDouble("exhibt_Review");
                        double score3 = infoScore.getDouble("service_Review");
                        score[0] = (score1+score2+score3)/3;
                        try{
                            it.put("muse_Score",score[0]);
                            sum[0]++;
                            museumShowArray.put(it);
                        }
                        catch (JSONException e){

                        }
                    }
                    catch(JSONException e){
                        score[0] = -1;
                        try{
                            it.put("muse_Score",score[0]);
                            sum[0]++;
                            museumShowArray.put(it);
                        }
                        catch (JSONException ee){

                        }
                    }
                    if(sum[0]==end){
                        museumShowArray=jsonArraySort(museumShowArray,"card_Pos");
                        generateMuseumBox(end-(indexNum-1)*num);
                    }

                }, (JSONObject error) -> {
                    // 请求失败
                    score[0] = -1;
                    try{
                        it.put("muse_Score",score[0]);
                        sum[0]++;
                        museumShowArray.put(it);
                    }
                    catch (JSONException e){

                    }
                    if(sum[0]==end){
                        museumShowArray=jsonArraySort(museumShowArray,"card_Pos");
                        generateMuseumBox(end-(indexNum-1)*num);
                    }
                });
            }
            catch(JSONException e){
                if(tip){
                    Toast.makeText(ctx, "数据请求出错", Toast.LENGTH_SHORT).show();
                }
            }
        }
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

            double scoreForm = it.getDouble("muse_Score");
            if(scoreForm<0){
                score="--";
            }
            else{
                score = String.format("%.1f",it.getDouble("muse_Score"));
            }
            image = it.getString("muse_Img");
            name = it.getString("muse_Name");
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
    private void generateMuseumBox(int num){
        //加载失败时的默认值
        String defaultImage = "";
        String defaultName = "暂无数据";
        String defaultScore = "0.0";
        for(int pos = 0;pos<num;pos++){
            try{
                JSONObject it = museumShowArray.getJSONObject(pos);
                String image;
                String name,text,grade;
                double gradeForm = it.getDouble("muse_Score");
                if(gradeForm<0){
                    grade="--";
                }
                else{
                    grade = String.format("%.1f",it.getDouble("muse_Score"));
                }
                image = it.getString("muse_Img");
                name = it.getString("muse_Name");
                text = it.getString("muse_Intro");
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
        museumShowArray = new JSONArray();
        indexNum+=1;
    }
    private void getMuseumArray(boolean tip){
        JSONArray museumScoreTrueArray = new JSONArray();
        JSONArray museumScoreFalseArray = new JSONArray();

        JSONObject params = new JSONObject();
        try{
            params.put("pageSize", 500);
            params.put("pageIndex", 1);
        }
        catch (JSONException e){

        }
        ApiTool.request(ctx, ApiPath.GET_ALL_MUSEUM_INFO, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                if(items.length()>0){
                    for(int i=0;i<items.length();i++){
                        JSONObject it = items.getJSONObject(i);
                        if(it.has("muse_Name")){
                            //museumScoreFalseArray.put(it);
                            boolean flag = false;
                            int id = it.getInt("muse_ID");
                            if(idMap.containsKey(id)){
                                JSONObject itScore = museumScoreTrueArray.getJSONObject(idMap.get(id));
                                itScore.put("muse_Intro",it.getString("muse_Intro"));
                                itScore.put("muse_Name",it.getString("muse_Name"));
                                itScore.put("muse_Img",it.getString("muse_Img"));
                            }
                            else{
                                museumScoreFalseArray.put(it);
                            }
                        }
                        else{
                            idMap.put(it.getInt("muse_ID"),museumScoreTrueArray.length());
                            museumScoreTrueArray.put(it);
                        }
                    }
                    for(int i=0;i<museumScoreTrueArray.length();i++){
                        museumArray.put(museumScoreTrueArray.get(i));
                    }
                    for(int i=0;i<museumScoreFalseArray.length();i++){
                        museumArray.put(museumScoreFalseArray.get(i));
                    }
                }
                else{
                    if(tip){
                        Toast.makeText(ctx, "没有数据", Toast.LENGTH_SHORT).show();
                    }
                }
                addMuseumBox(10,false);
            }
            catch(JSONException e){
                if(tip){
                    Toast.makeText(ctx, "数据缺失", Toast.LENGTH_SHORT).show();
                }
            }
        }, (JSONObject error) -> {
            // 请求失败
            if(tip){
                Toast.makeText(ctx, "全部数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public JSONArray jsonArraySort(JSONArray jsonArr,String sortKey) {
        JSONArray sortedJsonArray = new JSONArray();
        try{
            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonValues.add(jsonArr.getJSONObject(i));
            }
            Collections.sort( jsonValues, new Comparator<JSONObject>() {

                private final String KEY_NAME = sortKey;

                @Override
                public int compare(JSONObject a, JSONObject b) {
                    String valA="";
                    String valB="";

                    try {
                        valA = a.getInt(KEY_NAME)+"";
                        valB = b.getInt(KEY_NAME)+"";
                    }
                    catch (JSONException e) {
                        //do something
                    }

                    return valA.compareTo(valB);
                }
            });

            for (int i = 0; i < jsonArr.length(); i++) {
                sortedJsonArray.put(jsonValues.get(i));
            }
        }
        catch(JSONException e){

        }
        return sortedJsonArray;
    }
    public void openMuseumActivity(Context context,MuseumCard museumCard) {
        Intent intent = new Intent(context, MuseumActivity.class);

        TextView tv = museumCard.getMuseumName();
        String name = tv.getText().toString();
        try{
            for(int i=0;i<museumArray.length();i++){
                JSONObject it = museumArray.getJSONObject(i);
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
                if(name.equals(it.getString("muse_Name"))){
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
