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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import cn.edu.buct.se.cs1808.components.BoxTest;
import cn.edu.buct.se.cs1808.components.SearchCard;

public class SearchActivity extends AppCompatActivity {
    private ImageButton backButton;
    private EditText inputBox;
    private RoundImageView searchButton;
    private LinearLayout cardContainer;
    private ArrayList<TextView> typeArray = new ArrayList<>();
    private ArrayList<TextView> modeArray = new ArrayList<>();
    private TextView addText;
    private int mode;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_search,null);
        //默认值
        mode = 0;
        type = 0;

        backButton = (ImageButton) view.findViewById(R.id.activity_search_back);
        inputBox = (EditText) view.findViewById(R.id.activity_search_input);
        searchButton = (RoundImageView) view.findViewById(R.id.activity_search_button);
        cardContainer = (LinearLayout) view.findViewById(R.id.activity_search_card_container);
        addText = (TextView) view.findViewById(R.id.activity_search_addmore);
        typeArray.add((TextView) view.findViewById(R.id.activity_search_type1));
        typeArray.add((TextView) view.findViewById(R.id.activity_search_type2));
        typeArray.add((TextView) view.findViewById(R.id.activity_search_type3));
        modeArray.add((TextView) view.findViewById(R.id.activity_search_mode1));
        modeArray.add((TextView) view.findViewById(R.id.activity_search_mode2));

        typeChange((TextView) view.findViewById(R.id.activity_search_type1));
        modeChange((TextView) view.findViewById(R.id.activity_search_mode1));

        for(int i=0;i<typeArray.size();i++){
            TextView tv = typeArray.get(i);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    typeChange(tv);
                }
            });
        }
        for(int i=0;i<modeArray.size();i++){
            TextView tv = modeArray.get(i);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    modeChange(tv);
                }
            });
        }
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchLoad();
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
                addSearchCard(5);
            }
        });

        addSearchCard(10);
        setContentView(view);
    }
    private void addSearchCard(int num){
        int defaultImage = R.drawable.bleafumb_main_1;
        String defauleName = "测试记录";
        String defaultType = "博物馆";

        int image;
        String name,type;
        image = defaultImage;
        name = defauleName;
        type = defaultType;

        for(int i=0;i<num;i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            cardContainer.setLayoutParams(params);
            SearchCard searchCard = new SearchCard(this);
            searchCard.setAttr(image,name,type);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dip2px(this,370), dip2px(this,90));
            lp.setMargins(0, dip2px(this,10), 0, 0);
            searchCard.setLayoutParams(lp);
            searchCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    museumPage();
                    //test(searchCard);
                }
            });
            cardContainer.addView(searchCard);
        }
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    private void searchLoad(){
        viewRemove();
        addSearchCard(10);
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
    private void modeChange(TextView ele){
        for(int i=0;i<modeArray.size();i++){
            TextView tv = modeArray.get(i);
            if(tv==ele){
                tv.setBackgroundResource(R.drawable.bleafumb_orange_background);
                tv.setTextColor(android.graphics.Color.parseColor("#ffffff"));
                mode = i;
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
    //返回键点击事件
    public void backPage(){
        //暂定跳转回首页
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
    }
    public void museumPage(){
        Intent intent = new Intent(this, MuseumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
    }
}
