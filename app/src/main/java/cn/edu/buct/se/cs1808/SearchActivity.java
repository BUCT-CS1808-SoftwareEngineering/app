package cn.edu.buct.se.cs1808;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.RoundImageView;
import cn.edu.buct.se.cs1808.components.SearchCard;
import cn.edu.buct.se.cs1808.utils.JSONArraySort;

public class SearchActivity extends AppCompatActivity {
    private ImageButton backButton;
    private EditText inputBox;
    private RoundImageView searchButton;
    private LinearLayout cardContainer;
    private ArrayList<TextView> typeArray = new ArrayList<>();
    private TextView addText;
    private int type;
    private int typeForm;
    private int pageIndex;
    private String nameSearch;
    private JSONArray itemArray;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme(R.style.Theme_Secs1808);
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_search,null);
        //默认值
        type = 0;
        typeForm = -1;
        pageIndex = 1;
        nameSearch="";
        itemArray = new JSONArray();
        backButton = (ImageButton) view.findViewById(R.id.activity_search_back);
        inputBox = (EditText) view.findViewById(R.id.activity_search_input);
        searchButton = (RoundImageView) view.findViewById(R.id.activity_search_button);
        cardContainer = (LinearLayout) view.findViewById(R.id.activity_search_card_container);
        addText = (TextView) view.findViewById(R.id.activity_search_addmore);
        typeArray.add((TextView) view.findViewById(R.id.activity_search_type1));
        typeArray.add((TextView) view.findViewById(R.id.activity_search_type2));
        typeArray.add((TextView) view.findViewById(R.id.activity_search_type3));

        typeChange((TextView) view.findViewById(R.id.activity_search_type1));

        for(int i=0;i<typeArray.size();i++){
            TextView tv = typeArray.get(i);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeChange(tv);
                }
            });
        }
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSearch();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        addText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==0){
                    getMuseumData(10,nameSearch,true,true);
                }
                if(type==1){
                    getObjectData(10,nameSearch,true,true);
                }
                if(type==2){
                    getExhibitionData(10,nameSearch,true,true);
                }
            }
        });
        setContentView(view);
    }
    private void getMuseumData(int num,String name,boolean tip,boolean pageFlag){
        if(name.length()==0){
            return;
        }
        JSONObject params = new JSONObject();
        try{
            params.put("pageSize", num);
            params.put("pageIndex", pageIndex);
            params.put("muse_Name",name);
        }
        catch(JSONException e){

        }
        ApiTool.request(this, ApiPath.GET_ALL_MUSEUM_INFO, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                if(items.length()>0){
                    for(int i=0;i<items.length();i++){
                        JSONObject it = items.getJSONObject(i);
                        if(it.has("muse_Name")){
                            itemArray.put(it);
                        }
                    }
                    if(itemArray.length()>0){
                        itemArray = JSONArraySort.sort(name,itemArray);
                        generateSearchCard(10,0);
                    }
                    if(pageFlag){
                        pageIndex+=1;
                    }
                }
                else{
                    if(tip){
                        Toast.makeText(this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    }
                }
                typeForm = 0;
            }
            catch(JSONException e){

            }
        }, (JSONObject error) -> {
            // 请求失败

        });

    }
    private void getObjectData(int num,String name,boolean tip,boolean pageFlag){
        if(name.length()==0){
            return;
        }
        JSONObject params = new JSONObject();
        try{
            params.put("pageSize", num);
            params.put("pageIndex", pageIndex);
            params.put("col_Name",name);
        }
        catch(JSONException e){

        }
        ApiTool.request(this, ApiPath.GET_COLLECTION, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                if(items.length()>0){
                    for(int i=0;i<items.length();i++){
                        JSONObject it = items.getJSONObject(i);
                        itemArray.put(it);
                        //generateSearchCard(it,1);
                    }
                    if(itemArray.length()>0){
                        itemArray = JSONArraySort.sortByKey(name,itemArray,"col_Name");
                        generateSearchCard(10,1);
                    }
                    if(pageFlag){
                        pageIndex+=1;
                    }
                }
                else{
                    if(tip){
                        Toast.makeText(this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    }
                }
                typeForm = 1;
            }
            catch(JSONException e){

            }
        }, (JSONObject error) -> {
            // 请求失败

        });
    }
    private void getExhibitionData(int num,String name,boolean tip,boolean pageFlag){
        if(name.length()==0){
            return;
        }
        JSONObject params = new JSONObject();
        try{
            params.put("pageSize", num);
            params.put("pageIndex", pageIndex);
            params.put("exhib_Name",name);
        }
        catch(JSONException e){

        }
        ApiTool.request(this, ApiPath.GET_EXHIBITIONS, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                if(items.length()>0){
                    for(int i=0;i<items.length();i++){
                        JSONObject it = items.getJSONObject(i);
                        itemArray.put(it);
                        //generateSearchCard(it,2);
                    }
                    if(itemArray.length()>0){
                        itemArray = JSONArraySort.sortByKey(name,itemArray,"exhib_Name");
                        generateSearchCard(10,2);
                    }
                    if(pageFlag){
                        pageIndex+=1;
                    }
                }
                else{
                    if(tip){
                        Toast.makeText(this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    }
                }
                typeForm = 2;
            }
            catch(JSONException e){

            }
        }, (JSONObject error) -> {
            // 请求失败

        });
    }
    private void generateSearchCard(int num,int typeNum){
        String[] typeS = new String[3];
        typeS[0]="博物馆";
        typeS[1]="藏品";
        typeS[2]="展览";
        String image="";
        String name="暂无数据";
        for(int i=num*(pageIndex-1);i<itemArray.length();i++){
            try{
                JSONObject it = itemArray.getJSONObject(i);
                if(typeNum==0){
                    image = it.getString("muse_Img");
                    name = it.getString("muse_Name");
                }
                if(typeNum==1){
                    image = it.getString("col_Photo");
                    name = it.getString("col_Name").replaceAll("\\s*", "");
                }
                if(typeNum==2){
                    image = it.getString("exhib_Pic");
                    name = it.getString("exhib_Name").replaceAll("\\s*", "");
                }
            }
            catch(JSONException e){

            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardContainer.setLayoutParams(params);
            SearchCard searchCard = new SearchCard(this);
            searchCard.setAttr(image,name,typeS[typeNum]);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dip2px(this,370), dip2px(this,90));
            lp.setMargins(0, dip2px(this,10), 0, 0);
            searchCard.setLayoutParams(lp);
            if(type==0){
                searchCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        museumPage(searchCard);
                    }
                });
            }
            if(type==1){
                searchCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        objectPage(searchCard);
                    }
                });
            }
            if(type==2){
                searchCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        exhibitionPage(searchCard);
                    }
                });
            }
            cardContainer.addView(searchCard);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    private void typeChange(TextView ele){
        for(int i=0;i<typeArray.size();i++){
            TextView tv = typeArray.get(i);
            if(tv==ele){
                tv.setBackgroundResource(R.drawable.bleafumb_orange_background);
                tv.setTextColor(android.graphics.Color.parseColor("#ffffff"));
                type = i;
            }
            else{
                tv.setBackgroundResource(R.drawable.bleafumb_grey_background);
                tv.setTextColor(android.graphics.Color.parseColor("#000000"));
            }
        }
    }

    private void viewRemove(){
        cardContainer.removeAllViews();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cardContainer.setLayoutParams(params);
    }
    public void clickSearch(){
        String text=inputBox.getText().toString();
        boolean flagF = true;
        if(text.equals(nameSearch)&&type==typeForm){
            flagF = false;
        }
        else{
            itemArray = new JSONArray();
            viewRemove();
            pageIndex=1;
        }
        nameSearch = text;
        if(type==0){
            getMuseumData(10,text,true,flagF);
        }
        if(type==1){
            getObjectData(10,text,true,flagF);
        }
        if(type==2){
            getExhibitionData(10,text,true,flagF);
        }
    }
    //返回键点击事件
    public void backPage(){
        //暂定跳转回首页
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
    }
    public void museumPage(SearchCard searchCard){
        Intent intent = new Intent(this, MuseumActivity.class);
        String name=searchCard.getName().getText().toString();
        int museID = 1;
        try{
            for(int i=0;i<itemArray.length();i++){
                JSONObject it = itemArray.getJSONObject(i);
                if(it.getString("muse_Name").equals(name)){
                    museID = it.getInt("muse_ID");
                    break;
                }
            }
        }
        catch(JSONException e){

        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("muse_ID",museID);
        this.startActivity(intent);
    }

    public void objectPage(SearchCard searchCard){
        Intent intent = new Intent(this, DetailsObjectActivity.class);
        String name=searchCard.getName().getText().toString();
        int museID = 1;
        String imageObject="";
        String nameObject = "暂无数据";
        String content = "暂无数据";
        try{
            for(int i=0;i<itemArray.length();i++){
                JSONObject it = itemArray.getJSONObject(i);
                String nameN = it.getString("col_Name").replaceAll("\\s*", "");
                if(nameN.equals(name)){
                    museID = it.getInt("muse_ID");
                    imageObject = it.getString("col_Photo");
                    nameObject = name;
                    content = it.getString("col_Intro");
                    break;
                }
            }
        }
        catch(JSONException e){

        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("muse_ID",museID);
        intent.putExtra("col_Name",nameObject);
        intent.putExtra("col_Photo",imageObject);
        intent.putExtra("col_Intro",content);
        this.startActivity(intent);
    }
    public void exhibitionPage(SearchCard searchCard){
        Intent intent = new Intent(this, DetailsExhibitionActivity.class);
        String name=searchCard.getName().getText().toString();
        int museID = 1;
        String imageObject="";
        String nameObject = "暂无数据";
        String content = "暂无数据";
        try{
            for(int i=0;i<itemArray.length();i++){
                JSONObject it = itemArray.getJSONObject(i);
                String nameN = it.getString("exhib_Name").replaceAll("\\s*", "");
                if(nameN.equals(name)){
                    museID = it.getInt("muse_ID");
                    imageObject = it.getString("exhib_Pic");
                    nameObject = name;
                    content = it.getString("exhib_Content");
                    break;
                }
            }
        }
        catch(JSONException e){

        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("muse_ID",museID);
        intent.putExtra("exhib_Name",nameObject);
        intent.putExtra("exhib_Pic",imageObject);
        intent.putExtra("exhib_Content",content);
        this.startActivity(intent);
    }
}
