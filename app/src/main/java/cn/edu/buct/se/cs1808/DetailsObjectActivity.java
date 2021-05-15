package cn.edu.buct.se.cs1808;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.buct.se.cs1808.R;
import androidx.appcompat.app.AppCompatActivity;
public class DetailsObjectActivity extends AppCompatActivity {
    private boolean collectFlag;//收藏标志
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_details);
        collectFlag = false;//暂时假定未收藏。。。。。。。。具体要看数据库
        if(collectFlag){
            setCollectButtonBackground(true);
        }
        else{
            setCollectButtonBackground(false);
        }
        //收藏按钮事件绑定
        ImageView collectButton = (ImageView) findViewById(R.id.activity_object_details_collect);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectFlag = !collectFlag;
                if(collectFlag){
                    setCollectButtonBackground(true);
                }
                else{
                    setCollectButtonBackground(false);
                }
            }
        });
        //返回按钮事件绑定
        ImageView backButton = (ImageView) findViewById(R.id.activity_object_details_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });
        TextView exhibitionButton = (TextView) findViewById(R.id.activity_object_details);
        exhibitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backMuseum();
            }
        });
    }
    //收藏按钮点击反应
    private void setCollectButtonBackground(boolean mode){ //true转换收藏状态，false转换未收藏状态
        ImageView collectButton = (ImageView) findViewById(R.id.activity_object_details_collect);
        if(mode){
            collectButton.setBackgroundResource(R.drawable.bblk_collect_1);
        }
        else{
            collectButton.setBackgroundResource(R.drawable.bblk_collect_0);
        }
    }
    //返回键点击事件
    public void backPage(){
        //暂定跳转回首页
        Intent intent = new Intent(this, MuseumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
    }
    public void backMuseum(){
        //暂定跳转回首页
        Intent intent = new Intent(this, MuseumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
    }
}
