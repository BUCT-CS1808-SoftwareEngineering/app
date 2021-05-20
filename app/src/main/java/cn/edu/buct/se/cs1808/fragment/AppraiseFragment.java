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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.edu.buct.se.cs1808.MainActivity;
import cn.edu.buct.se.cs1808.MuseumActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;
import cn.edu.buct.se.cs1808.SubmitAppraiseActivity;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.AppraiseCard;
import cn.edu.buct.se.cs1808.components.AppraiseScore;
import cn.edu.buct.se.cs1808.components.BoxTest;

public class AppraiseFragment extends Fragment{
    private int activityId;
    private Context ctx;
    private AppraiseScore myScore;
    private ArrayList<TextView> buttonArray;
    private TextView appraiseNumber;
    private LinearLayout appraiseContainer;
    private TextView appraiseSubmit;
    private int museID;

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


        appraiseModeChange((TextView) view.findViewById(R.id.appraise_activity_allappraise));
        pageIndex = 1;
        commentJSONArray = new JSONArray();
        getUserInfo();

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
                openSubmitActivity(ctx);
            }
        });
    }

    //评价类型切换
    private void appraiseModeChange(TextView ele){
        appraiseContainer.removeAllViews();
        for(int i=0;i<buttonArray.size();i++){
            TextView tv = buttonArray.get(i);
            if(tv==ele){
                tv.setBackgroundResource(R.drawable.common_orange_background);
                tv.setTextColor(android.graphics.Color.parseColor("#ffffff"));
            }
            else{
                tv.setBackgroundResource(R.color.white);
                tv.setTextColor(android.graphics.Color.parseColor("#000000"));
            }
        }
        addAppraiseCard(10);
    }
    //设置评价条数
    private void setAppraiseNumber(int num){
        appraiseNumber.setText(num+"条");
    }
    //添加评价记录
    private void addAppraiseCard(int num){
        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", num);
            params.put("pageIndex", pageIndex);
            params.put("muse_ID",museID);
        }
        catch (JSONException e) {

        }
        ApiTool.request(ctx, ApiPath. GET_COMMENT, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                for(int i=0;i<items.length();i++){
                    JSONObject it = items.getJSONObject(i);
                    int flag = it.getInt("com_IfShow");
                    if(flag==0){
                        continue;
                    }
                    String userName="",userImg="";
                    try{
                        int userID = it.getInt("user_ID");
                        for(int j=0;j<userJSONArray.length();j++){
                            JSONObject userit = userJSONArray.getJSONObject(i);
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

                }
                pageIndex+=1;
            }
            catch(JSONException e){
            }

        }, (JSONObject error) -> {
            // 请求失败

        });
    }
    private void generateAppraiseCard(int num){
        int defaultImage = R.drawable.bleafumb_object;
        String defauleName = "bleafumb";
        String defaultTime = "2021-4-30";
        String defaultComment = "文法G的所有有效项目集组成的集合，称为G的LR(0)项目集规范族。n重独立试验：若n个独立试验是相同的，则称其为n重独立试验。若每次试验的结果只有两个或，则称其为n重贝努利试验。";
        int defaultScore=4;
        int image,score;
        String name,comment,time;
        image = defaultImage;
        comment = defaultComment;
        name = defauleName;
        score = defaultScore;
        time = defaultTime;
        for(int i=0;i<num;i++) {
            AppraiseCard appraiseCard = new AppraiseCard(ctx);
            appraiseCard.setAttr(image, name, time, score, comment);
            RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, dip2px(ctx, 10), 0, dip2px(ctx, 10));
            appraiseCard.setLayoutParams(lp);
            appraiseContainer.addView(appraiseCard);
        }
    }
    private void getFeedback(int userId){
        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", 1);
            params.put("pageIndex", 1);
            params.put("muse_ID",museID);
            params.put("user_ID",userId);
        }
        catch (JSONException e) {

        }
        ApiTool.request(ctx, ApiPath. GET_USER_SCORE, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                JSONObject it = items.getJSONObject(0);

            }
            catch(JSONException e){
            }

        }, (JSONObject error) -> {
            // 请求失败

        });
    }
    private void getUserInfo(){
        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", 65536);
            params.put("pageIndex", 1);
        }
        catch (JSONException e) {

        }
        ApiTool.request(ctx, ApiPath. GET_COMMENT, params, (JSONObject rep) -> {
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
            }
            catch(JSONException e){
            }

        }, (JSONObject error) -> {
            // 请求失败

        });
    }
    //dp转px
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //跳转评价界面
    public static void openSubmitActivity(Context context) {
        //页面跳转
        Intent intent = new Intent(context, SubmitAppraiseActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
