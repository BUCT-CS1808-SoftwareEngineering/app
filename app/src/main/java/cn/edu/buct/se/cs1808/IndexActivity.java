package cn.edu.buct.se.cs1808;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.buct.se.cs1808.components.BoxTest;
import cn.edu.buct.se.cs1808.components.MuseumCard;

public class IndexActivity extends AppCompatActivity {
    private LinearLayout searchContainer;
    private LinearLayout museumContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_index, null);
        searchContainer = (LinearLayout) view.findViewById(R.id.main_search_container);
        museumContainer = (LinearLayout) view.findViewById(R.id.main_museum_container);
        addSearchBox(10);
        addMuseumBox(10);

        TextView lookMore = view.findViewById(R.id.main_text_more);
        TextView lead = view.findViewById(R.id.main_text_lead);
        TextView hotLead = view.findViewById(R.id.main_hot_lead);
        TextView museumLead = view.findViewById(R.id.main_museum_lead);
        lookMore.setTextColor(android.graphics.Color.parseColor("#ee7712"));
        lead.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        hotLead.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        museumLead.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        setContentView(view);
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    private void addSearchBox(int num){
        int defaultImage = R.drawable.bleafumb_main_2;
        String defauleName = "测试博物馆";
        String defaultScore = "9.9";
        int defaultGrade=4;
        int image,grade;
        String name,score;
        image = defaultImage;
        grade = defaultGrade;
        name = defauleName;
        score = defaultScore;
        for(int i=0;i<num;i++){
            grade = (i+1);
            BoxTest searchBox = new BoxTest(this);
            searchBox.setAttr(image,name,grade,score);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dip2px(this,120), dip2px(this,150));
            lp.setMargins(0, 0, dip2px(this,10), 0);
            searchBox.setLayoutParams(lp);
            searchContainer.addView(searchBox);
        }
    }
    private void addMuseumBox(int num){
        int defaultImage = R.drawable.bleafumb_main_3;
        String defauleName = "测试博物馆";
        String defaultScore = "9.9";
        String defaultText = "测试我呢比为巴西被群殴还不行编译哦对十八度不行哦行不行哦亲比一般先擦需要";
        int image;
        String name,text,score;
        image = defaultImage;
        text = defaultText;
        name = defauleName;
        score = defaultScore;
        for(int i=0;i<num;i++){
            MuseumCard museumCard = new MuseumCard(this);
            museumCard.setAttr(image,name,text,score);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            museumCard.setLayoutParams(params);
            museumContainer.addView(museumCard);
        }
    }
}
