package cn.edu.buct.se.cs1808.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.SubmitAppraiseActivity;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.AppraiseCard;
import cn.edu.buct.se.cs1808.components.AppraiseScore;
import cn.edu.buct.se.cs1808.utils.User;

public class AppraiseFragment extends Fragment{
    private int activityId;
    private Context ctx;
    private AppraiseScore myScore;
    private ArrayList<TextView> buttonArray;
    private TextView appraiseNumber;
    private TextView addText;
    private LinearLayout appraiseContainer;
    private TextView appraiseSubmit;
    private int museID;
    private int type;
    private boolean loginFlag;
    private int pageSize=10;

    private int pageIndex;
    private JSONArray commentJSONArray;
    private JSONArray userJSONArray;

    public AppraiseFragment(){
        activityId = R.layout.activity_appraise;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ctx = getContext();

        Bundle bundle=getArguments();
        if(bundle!=null){
            museID = bundle.getInt("muse_ID");
        }
        return inflater.inflate(activityId, container, false);
    }
    @Override
    public void onViewCreated( @Nullable View view, Bundle savedInstanceState) {
        //元素获取
        buttonArray = new ArrayList<>();
        buttonArray.add((TextView) view.findViewById(R.id.appraise_activity_allappraise));
        buttonArray.add((TextView) view.findViewById(R.id.appraise_activity_goodappraise));
        buttonArray.add((TextView) view.findViewById(R.id.appraise_activity_midappraise));
        buttonArray.add((TextView) view.findViewById(R.id.appraise_activity_badappraise));
        myScore = (AppraiseScore) view.findViewById(R.id.appraise_activity_myscore);
        appraiseNumber = (TextView) view.findViewById(R.id.appraise_activity_number);
        appraiseContainer = (LinearLayout) view.findViewById(R.id.appraise_activity_container);
        appraiseSubmit = (TextView) view.findViewById(R.id.appraise_activity_add);
        addText = (TextView) view.findViewById(R.id.appraise_addmore);


        appraiseModeChange((TextView) view.findViewById(R.id.appraise_activity_allappraise));
        pageIndex = 1;
        type = 0;
        commentJSONArray = new JSONArray();
        userJSONArray = new JSONArray();
        getUserInfo();


        loginFlag = User.isLogin(ctx);

        setMyScore();

        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAppraiseCard(10,true);
            }
        });
        //评价切换事件绑定
        for(int i=0;i<buttonArray.size();i++){
            TextView tv = buttonArray.get(i);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appraiseModeChange(tv);
                }
            });
        }
        //点击评价
        appraiseSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!loginFlag){
                    Toast.makeText(ctx, "未登录用户无法评论", Toast.LENGTH_SHORT).show();
                }
                else{
                    openSubmitActivity(ctx);
                }
            }
        });
    }


    //评价类型切换
    private void appraiseModeChange(TextView ele){
        int num = 0;
        for(int i=0;i<buttonArray.size();i++){
            TextView tv = buttonArray.get(i);
            if(tv==ele){
                tv.setBackgroundResource(R.drawable.common_orange_background);
                tv.setTextColor(android.graphics.Color.parseColor("#ffffff"));
                num = i;
            }
            else{
                tv.setBackgroundResource(R.color.white);
                tv.setTextColor(android.graphics.Color.parseColor("#000000"));
            }
        }
        if(type!=num){
            appraiseContainer.removeAllViews();
            pageIndex = 1;
            commentJSONArray = new JSONArray();
            type = num;
            addAppraiseCard(10,true);
        }
    }
    //设置评价条数
    private void setAppraiseNumber(int num){
        appraiseNumber.setText(num+"条");
    }
    //添加评价记录
    private void addAppraiseCard(int num,boolean tip){
        JSONObject params = new JSONObject();
        int maxn=5;
        int minn=0;
        if(type==1){
            minn = 4;
            maxn = 5;
        }
        else if(type==2){
            minn = 2;
            maxn = 3;
        }
        else if(type==3){
            minn = 0;
            maxn = 1;
        }
        try {
            params.put("pageSize", num);
            params.put("pageIndex", pageIndex);
            params.put("muse_ID",museID);
            params.put("min_review", minn);
            params.put("max_review", maxn);
        }
        catch (JSONException e) {

        }
        ApiTool.request(ctx, ApiPath. GET_COMMENT_BY_SCORE, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                setAppraiseNumber(info.getInt("num"));
                JSONArray items = info.getJSONArray("items");
                int[] sum = new int[1];
                sum[0]=0;
                if(items.length()>0){
                    for(int i=0;i<items.length();i++){
                        JSONObject it = items.getJSONObject(i);
                        it.put("card_Pos",(pageIndex-1)*num+i);
                        String userName="",userImg="";
                        double score=-1;
                        try{
                            int userID = it.getInt("user_ID");
                            for(int j=0;j<userJSONArray.length();j++){
                                JSONObject userit = userJSONArray.getJSONObject(j);
                                if (userID==userit.getInt("user_ID")){
                                    userName = userit.getString("user_Name");
                                    userImg = userit.getString("user_Avatar");
                                }
                            }
                        }
                        catch(JSONException e){
                            userName = "佚名";
                            userImg="";
                        }
                        it.put("user_Name",userName);
                        it.put("user_Img",userImg);
                        JSONObject paramsScore = new JSONObject();
                        try {
                            paramsScore.put("pageSize", 1);
                            paramsScore.put("pageIndex", 1);
                            paramsScore.put("muse_ID",museID);
                            paramsScore.put("user_ID",it.getInt("user_ID"));
                        }
                        catch (JSONException e) {

                        }
                        double [] scoreUser = new double[1];
                        ApiTool.request(ctx, ApiPath. GET_USER_SCORE, paramsScore, (JSONObject repScore) -> {
                            // 请求成功，rep为请求获得的数据对象
                            sum[0]++;
                            try{
                                JSONObject infoScore = repScore.getJSONObject("info");
                                JSONArray itemsScore = infoScore.getJSONArray("items");
                                JSONObject itScore = itemsScore.getJSONObject(0);
                                double score1 = itScore.getDouble("env_Review");
                                double score2 = itScore.getDouble("exhibt_Review");
                                double score3 = itScore.getDouble("service_Review");
                                scoreUser[0] = (score1+score2+score3)/3.0;
                                try{
                                    it.put("muse_Score",scoreUser[0]);
                                    //generateAppraiseCard(it);
                                    commentJSONArray.put(it);
                                }
                                catch (JSONException e){

                                }

                            }
                            catch(JSONException e){
                                try{
                                    it.put("muse_Score",0);
                                    //generateAppraiseCard(it);
                                    commentJSONArray.put(it);
                                }
                                catch (JSONException ee){

                                }
                            }
                            if(sum[0]==items.length()){
                                commentJSONArray=jsonArraySort(commentJSONArray,"card_Pos");
                                generateAppraiseCard(sum[0]);
                            }

                        }, (JSONObject error) -> {
                            // 请求失败
                            sum[0]++;
                            try{
                                it.put("muse_Score",0);
                                //generateAppraiseCard(it);
                                commentJSONArray.put(it);
                            }
                            catch (JSONException e){

                            }
                            if(sum[0]==items.length()){
                                commentJSONArray=jsonArraySort(commentJSONArray,"card_Pos");
                                generateAppraiseCard(sum[0]);
                            }
                        });
                    }
                }
                else{
                    if(tip){
                        Toast.makeText(ctx, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch(JSONException e){
            }

        }, (JSONObject error) -> {
            // 请求失败

        });
    }
    private void generateAppraiseCard(int num){
        double score;
        String image,name,comment,time;
        image = "";
        comment = "";
        name = "暂无数据";
        score = 0;
        time = "";
        for(int i=(pageIndex-1)*pageSize;i<(pageIndex-1)*pageSize+num;i++){
            try{
                JSONObject it = commentJSONArray.getJSONObject(i);
                image = ApiTool.getADDRESS()+it.getString("user_Img");
                name = it.getString("user_Name");
                comment = it.getString("com_Info");
                score = it.getDouble("muse_Score");
                time = it.getString("com_Time").substring(0,10);
            }
            catch (JSONException e){

            }

            AppraiseCard appraiseCard = new AppraiseCard(ctx);
            appraiseCard.setAttr(image, name, time, (int)Math.round(score), comment);
            RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, dip2px(ctx, 10), 0, dip2px(ctx, 10));
            appraiseCard.setLayoutParams(lp);
            appraiseContainer.addView(appraiseCard);
        }
        pageIndex+=1;
    }
    private void getUserInfo(){
        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", 65536);
            params.put("pageIndex", 1);
        }
        catch (JSONException e) {

        }
        ApiTool.request(ctx, ApiPath. GET_USER_INFO, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                for(int i=0;i<items.length();i++){
                    try{
                        JSONObject it = items.getJSONObject(i);
                        JSONObject userJSON = new JSONObject();
                        userJSON.put("user_ID",it.getInt("user_ID"));
                        userJSON.put("user_Name",it.getString("user_Name"));
                        userJSON.put("user_Avatar",it.getString("user_Avatar"));
                        userJSONArray.put(userJSONArray.length(),userJSON);
                    }
                    catch(JSONException e){
                        
                    }
                }
                addAppraiseCard(10,false);
            }
            catch(JSONException e){
            }

        }, (JSONObject error) -> {
            // 请求失败

        });
    }
    private void setMyScore(){
        if(loginFlag){
            int[] scoreUser = new int[1];
            scoreUser[0]=0;
            JSONObject userInfo = User.getUserInfo(ctx);
            JSONObject paramsScore = new JSONObject();
            try {
                paramsScore.put("pageSize", 1);
                paramsScore.put("pageIndex", 1);
                paramsScore.put("muse_ID",museID);
                paramsScore.put("user_ID",userInfo.getString("user_ID"));
            }
            catch (JSONException e) {

            }
            ApiTool.request(ctx, ApiPath. GET_USER_SCORE, paramsScore, (JSONObject repScore) -> {
                // 请求成功，rep为请求获得的数据对象
                try{
                    JSONObject infoScore = repScore.getJSONObject("info");
                    JSONArray itemsScore = infoScore.getJSONArray("items");
                    JSONObject itScore = itemsScore.getJSONObject(0);
                    double score1 = itScore.getDouble("env_Review");
                    double score2 = itScore.getDouble("exhibt_Review");
                    double score3 = itScore.getDouble("service_Review");
                    scoreUser[0] = (int)Math.round((score1+score2+score3)/3.0);
                    myScore.setScore(scoreUser[0]);
                }
                catch(JSONException e){
                    myScore.setScore(0);
                }

            }, (JSONObject error) -> {
                // 请求失败
                myScore.setScore(0);
            });
        }
        else{
            myScore.setScore(0);
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
    //dp转px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //跳转评价界面
    public void openSubmitActivity(Context context) {
        //页面跳转
        JSONObject userInfo = User.getUserInfo(context);
        Intent intent = new Intent(context, SubmitAppraiseActivity.class);
        try{
            intent.putExtra("user_ID",userInfo.getInt("user_ID"));
            intent.putExtra("muse_ID",museID);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            context.startActivity(intent);
        }
        catch(JSONException e){
            Toast.makeText(context, "获取用户信息失败", Toast.LENGTH_SHORT).show();
        }

    }
}
