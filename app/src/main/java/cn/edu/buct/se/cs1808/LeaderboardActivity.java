package cn.edu.buct.se.cs1808;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    }
    private void addLeaderboardBox(int num){
        int defaultImage = R.drawable.bleafumb_main_3;
        String defauleName = "故宫博物院";
        String defauleScore = "9.8";
        String defauleNum = " ";
        int image;
        String name;
        String score;
        String l_num;
        image = defaultImage;
        name = defauleName;
        score = defauleScore;
        l_num = defauleNum;
        for(int i=1;i<=num;i++){
            LeaderboardCard leaderboardCard=new LeaderboardCard(this);
            //设置属性
            leaderboardCard.setAttr(image,name,score,l_num+i);
            RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            leaderboardCard.setLayoutParams(params);
            RoundImageView rImage = leaderboardCard.getImage();
            TextView mName = leaderboardCard.getName();
            TextView Score = leaderboardCard.getScore();
            leaderboardContainer.addView(leaderboardCard);
        }
    }
}
