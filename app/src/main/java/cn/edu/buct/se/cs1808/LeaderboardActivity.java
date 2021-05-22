package cn.edu.buct.se.cs1808;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.LeaderboardCard;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    private LinearLayout leaderboardContainer;
    private JSONArray itemsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_leaderboard,null);
        leaderboardContainer = (LinearLayout) view.findViewById(R.id.main_leaderboard_card);
        itemsArray = new JSONArray();
        addLeaderboardBox(10);
        setContentView(view);
        //返回按钮事件绑定
        ImageView backButton = (ImageView) findViewById(R.id.activity_leaderboard_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });
    }
    private void addLeaderboardBox(int num){
        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", num);
            params.put("pageIndex", 1);
            params.put("muse_Name", ".");
        }
        catch (JSONException e) {

        }
        ApiTool.request(this, ApiPath.GET_MUSEUM_BY_CLICK, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                int sum[] = new int[1];
                sum[0]=0;
                for(int i=0;i<items.length();i++){
                    JSONObject it = items.getJSONObject(i);
                    it.put("card_Pos",i);
                    double score[] = new double[1];
                    JSONObject scoreParams = new JSONObject();
                    try{
                        scoreParams.put("muse_ID",it.getInt("muse_ID"));
                    }
                    catch (JSONException e){

                    }
                    ApiTool.request(this, ApiPath.GET_MUSEUM_SCORE, scoreParams, (JSONObject repScore) -> {
                        // 请求成功，rep为请求获得的数据对象
                        sum[0]++;
                        try{
                            JSONObject infoScore = repScore.getJSONObject("info");
                            double score1 = infoScore.getDouble("env_Review");
                            double score2 = infoScore.getDouble("exhibt_Review");
                            double score3 = infoScore.getDouble("service_Review");
                            score[0] = (score1+score2+score3)/3;
                            try{
                                it.put("muse_Score",score[0]);
                                itemsArray.put(itemsArray.length(),it);
                            }
                            catch (JSONException e){

                            }
                        }
                        catch(JSONException e){
                            score[0] = -1;
                            try{
                                it.put("muse_Score",score[0]);
                                itemsArray.put(itemsArray.length(),it);
                            }
                            catch (JSONException ee){

                            }
                        }
                        if(sum[0]==items.length()){
                            itemsArray=jsonArraySort(itemsArray,"card_Pos");
                            generateLeaderboardBox(num);
                        }
                    }, (JSONObject error) -> {
                        // 请求失败
                        sum[0]++;
                        score[0] = -1;
                        try{
                            it.put("muse_Score",score[0]);
                            itemsArray.put(itemsArray.length(),it);
                        }
                        catch (JSONException e){

                        }
                        if(sum[0]==items.length()){
                            itemsArray=jsonArraySort(itemsArray,"card_Pos");
                            generateLeaderboardBox(num);
                        }
                    });
                }
            }
            catch(JSONException e){

            }
        }, (JSONObject error) -> {
            // 请求失败

        });
    }
    private void generateLeaderboardBox(int num){
        String image = "";
        String name="暂无数据";
        String score="--";
        String l_num="--";
        int id=1;
        int button=R.drawable.bblk_details;
        for(int i=0;i<itemsArray.length();i++){
            try{
                JSONObject it = itemsArray.getJSONObject(i);
                image = it.getString("muse_Img");
                name = it.getString("muse_Name");
                id = it.getInt("muse_ID");
                int numP = it.getInt("card_Pos")+1;
                l_num = numP+"";
                double scoreForm = it.getDouble("muse_Score");
                if(scoreForm<0){
                    score="--";
                }
                else{
                    score = String.format("%.1f",it.getDouble("muse_Score"));
                }
            }
            catch(JSONException e){

            }
            LeaderboardCard leaderboardCard=new LeaderboardCard(this);
            //设置属性
            leaderboardCard.museID = id;
            leaderboardCard.setAttr(image,name,score,l_num,button);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            leaderboardCard.setLayoutParams(params);
            RoundImageView rImage = leaderboardCard.getImage();
            TextView mName = leaderboardCard.getName();
            TextView Score = leaderboardCard.getScore();
            ImageView lButton = leaderboardCard.getButton();
            leaderboardCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMuseumActivity(getBaseContext(),leaderboardCard);
                }
            });
            leaderboardContainer.addView(leaderboardCard);
        }
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
    public static void openMuseumActivity(Context context,LeaderboardCard leaderboardCard) {
        //页面跳转
        Intent intent = new Intent(context, MuseumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("muse_ID",leaderboardCard.museID);
        context.startActivity(intent);
    }
    //返回键点击事件
    public void backPage(){
        //暂定跳转回首页
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
    }
}
