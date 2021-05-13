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
import cn.edu.buct.se.cs1808.components.LeaderboardCard;
import androidx.appcompat.app.AppCompatActivity;
public class LeaderboardActivity extends AppCompatActivity {
    private LinearLayout leaderboardContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(this).inflate(R.layout.activity_leaderboard,null);
        leaderboardContainer = (LinearLayout) view.findViewById(R.id.main_leaderboard_card);
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
        int defaultImage = R.drawable.bleafumb_main_3;
        String defauleName = "故宫博物院";
        String defauleScore = "9.8";
        String defauleNum = " ";
        int defaultButton = R.drawable.bblk_details;
        int image;
        String name;
        String score;
        String l_num;
        int button;
        image = defaultImage;
        name = defauleName;
        score = defauleScore;
        l_num = defauleNum;
        button = defaultButton;
        for(int i=1;i<=num;i++){
            LeaderboardCard leaderboardCard=new LeaderboardCard(this);
            //设置属性
            leaderboardCard.setAttr(image,name,score,l_num+i,button);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            leaderboardCard.setLayoutParams(params);
            RoundImageView rImage = leaderboardCard.getImage();
            TextView mName = leaderboardCard.getName();
            TextView Score = leaderboardCard.getScore();
            ImageView lButton = leaderboardCard.getButton();
            lButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMuseumActivity(getBaseContext());
                }
            });
            leaderboardContainer.addView(leaderboardCard);
        }
    }
    public static void openMuseumActivity(Context context) {
        //页面跳转
        Intent intent = new Intent(context, MuseumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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
