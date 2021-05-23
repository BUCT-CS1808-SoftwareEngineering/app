package cn.edu.buct.se.cs1808;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.utils.JsonFileHandler;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.ReduceHtml2Text;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Year;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailsEducationActivity extends AppCompatActivity {
    private ImageView educationImage;
    private TextView educationTitle;
    private TextView educationContent;
    private TextView museumText;
    private ImageButton backButton;

    private int museID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Secs1808);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_details);


        educationImage = (ImageView) findViewById(R.id.education_detail_image);
        educationTitle = (TextView) findViewById(R.id.education_detail_title);
        educationContent = (TextView) findViewById(R.id.education_detail_content);
        museumText = (TextView) findViewById(R.id.education_detail_museum);
        backButton = (ImageButton) findViewById(R.id.education_detail_back);

        Intent intent = getIntent();
        museID = intent.getIntExtra("muse_ID",-1);
        String image = intent.getStringExtra("act_Pic");
        String name = intent.getStringExtra("act_Name");
        String content = intent.getStringExtra("act_Content");
        content = ReduceHtml2Text.removeHtmlTag(content);
        showInfo(image,name,content);
        setMuseum();


        //返回按钮事件绑定
        ImageView backButton = (ImageView) findViewById(R.id.education_detail_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMuseum();
            }
        });

        TextView exhibitionButton = (TextView) findViewById(R.id.education_detail_museum);
        exhibitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMuseum();
            }
        });
    }
    private void showInfo(String image,String name,String content){
        educationTitle.setText(name);
        String contentFilt="";
        if(content!=null){
            Pattern p = Pattern.compile("(\r?\n(\\s*\r?\n)+)");
            Matcher m = p.matcher(content);
            contentFilt = m.replaceAll("\r\n");
        }
        educationContent.setText(contentFilt);
        LoadImage loader = new LoadImage(educationImage);
        loader.setBitmap(image);
    }
    private void setMuseum(){
        JSONObject params = new JSONObject();
        try{
            params.put("muse_ID",museID);
            params.put("pageIndex",1);
            params.put("pageSize",1);
        }
        catch (JSONException e){

        }
        ApiTool.request(this,ApiPath.GET_MUSEUM_INFO,params,(JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            String name = "暂无数据";
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                JSONObject it = items.getJSONObject(0);
                name = it.getString("muse_Name");
                museumText.setText(name);
            }
            catch(JSONException e){
                museumText.setText(name);

            }
        }, (JSONObject error) -> {
            // 请求失败
            museumText.setText("暂无数据");
        });

    }
    //返回键点击事件
    public void backMuseum(){
        Intent intent = new Intent(this, MuseumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("muse_ID",museID);
        this.startActivity(intent);
        this.finish();
    }
}
