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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.DetailsObjectActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.components.RoundImageView;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.ObjectCard;

public class ObjectFragment extends NavBaseFragment{
    private LinearLayout objectContainer;
    private int museID;
    private int indexNum;
    private JSONArray objectJSONArray;
    private TextView addmore;
    public ObjectFragment(){
        activityId = R.layout.activity_object;
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
        objectContainer = (LinearLayout)view.findViewById(R.id.main_object_container);
        addmore = (TextView)view.findViewById(R.id.object_addmore);
        indexNum = 1;
        objectJSONArray = new JSONArray();
        addObjectBox(3,false);
        addmore.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addObjectBox(3,true);
            }
        });
    }
    private void addObjectBox(int num,boolean tip){
        JSONObject params = new JSONObject();
        try{
            params.put("pageSize",num);
            params.put("pageIndex",indexNum);
            params.put("muse_ID",museID);
        }catch (JSONException e){

        }
        ApiTool.request(ctx, ApiPath.GET_COLLECTION,params,(JSONObject rep)->{
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                if(items.length()>0){
                    for(int i=0;i<items.length();i++){
                        JSONObject it = items.getJSONObject(i);
                        objectJSONArray.put(objectJSONArray.length(),it);
                        generateObjectBox(it);
                    }
                    indexNum+=1;
                }
                else{
                    if(tip){
                        Toast.makeText(ctx,"没有更多数据了",Toast.LENGTH_SHORT).show();
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
    private void generateObjectBox(JSONObject it){
        String image="";
        String name="暂无数据";
        try {
            image =it.getString("col_Photo");
            name =it.getString("col_Name").replaceAll("\\s*", "");
        }
        catch (JSONException e){
            image ="";
            name = "暂无数据";
        }
        ObjectCard objectCard=new ObjectCard(ctx);
        //设置属性
        objectCard.setAttr(image,name);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        objectCard.setLayoutParams(params);
        //获取自定义类内元素绑定事件
        RoundImageView rImage = objectCard.getMuseumImage();
        TextView mName = objectCard.getMuseumName();
        rImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetailsObjectActivity(ctx,mName);
            }
        });
        objectContainer.addView(objectCard);
    }
    public void openDetailsObjectActivity(Context context,TextView textView) {
        //页面跳转
        Intent intent = new Intent(context, DetailsObjectActivity.class);
        String name = textView.getText().toString();
        try{
            for(int i=0;i<objectJSONArray.length();i++){
                JSONObject it = objectJSONArray.getJSONObject(i);
                String nameJSON = it.getString("col_Name").replaceAll("\\s*", "");
                if(nameJSON.equals(name)){
                    intent.putExtra("col_Name",name);
                    intent.putExtra("col_Intro",it.getString("col_Intro"));
                    intent.putExtra("col_Photo",it.getString("col_Photo"));
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
