package cn.edu.buct.se.cs1808.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.DetailsEducationActivity;
import cn.edu.buct.se.cs1808.DetailsExhibitionActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.ExhibitionCard;

public class ExhibitionFragment extends NavBaseFragment{
    private LinearLayout exhibitionContainer;
    private int museID;
    private int indexNum;
    private JSONArray exhibitionJSONArray;
    private TextView addmore;
    public ExhibitionFragment(){
        activityId = R.layout.activity_exhibition;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ctx =getContext();
        Bundle bundle = getArguments();
        if(bundle!=null){
            museID = bundle.getInt("muse_ID");
        }
        return inflater.inflate(activityId,container,false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        exhibitionContainer = (LinearLayout)view.findViewById(R.id.main_exhibition_container);
        addmore = (TextView)view.findViewById(R.id.exhibition_addmore);
        indexNum = 1;
        exhibitionJSONArray = new JSONArray();
        addExhibitionBox(5,false);
        addmore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addExhibitionBox(5,true);
            }
        });
    }
    private void addExhibitionBox(int num,boolean tip){
        JSONObject params = new JSONObject();
        try{
            params.put("pageSize",num);
            params.put("pageIndex",indexNum);
            params.put("muse_ID",museID);
        }catch (JSONException e){

        }
        ApiTool.request(ctx, ApiPath.GET_EXHIBITIONS,params,(JSONObject rep)->{
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                if(items.length()>0){
                    for(int i=0;i<items.length();i++){
                        JSONObject it = items.getJSONObject(i);
                        exhibitionJSONArray.put(exhibitionJSONArray.length(),it);
                        generateExhibitionBox(it);
                    }
                    indexNum+=1;
                }
                else{
                    if(tip){
                        Toast.makeText(ctx, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            catch(JSONException e){
                if(tip){
                    Toast.makeText(ctx, "数据请求出错", Toast.LENGTH_SHORT).show();
                }
            }
        }, (JSONObject error) -> {
            // 请求失败
            if(tip){
                Toast.makeText(ctx, "数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void generateExhibitionBox(JSONObject it){
        String image="";
        String name="暂无数据";
        try {
            image =it.getString("exhib_Pic");
            name =it.getString("exhib_Name").replaceAll("\\s*", "");
        }
        catch (JSONException e){
            image ="";
            name = "暂无数据";
        }
        ExhibitionCard exhibitionCard=new ExhibitionCard(ctx);
        //设置属性
        exhibitionCard.setAttr(image,name);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        exhibitionCard.setLayoutParams(params);
        //获取自定义类内元素绑定事件
        RoundImageView rImage = exhibitionCard.getMuseumImage();
        TextView mName = exhibitionCard.getMuseumName();
        rImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailsExhibitionActivity(ctx,mName);
            }
        });
        exhibitionContainer.addView(exhibitionCard);
    }
    public void openDetailsExhibitionActivity(Context context,TextView textView) {
        //页面跳转
        Intent intent = new Intent(context, DetailsExhibitionActivity.class);
        String name = textView.getText().toString();
        try{
            for(int i=0;i<exhibitionJSONArray.length();i++){
                JSONObject it = exhibitionJSONArray.getJSONObject(i);
                String nameJSON = it.getString("exhib_Name").replaceAll("\\s*", "");
                if(nameJSON.equals(name)){
                    intent.putExtra("exhib_Name",name);
                    intent.putExtra("exhib_Content",it.getString("exhib_Content"));
                    intent.putExtra("exhib_Pic",it.getString("exhib_Pic"));
                    intent.putExtra("muse_ID",museID);
                    break;
                }
            }
        }
        catch(JSONException e){

        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
    @Override
    public int getItemId() {
        return R.id.navigation_home;
    }
}
