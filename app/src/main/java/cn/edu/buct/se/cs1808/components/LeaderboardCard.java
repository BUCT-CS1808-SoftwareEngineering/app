package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;

public class LeaderboardCard extends LinearLayout {
    private RoundImageView leaderboardImage;
    private TextView leaderboardName;
    private TextView leaderboardScore;
    private TextView leaderboardNum;
    private View rootView;
  //  private int textCutLen=55;
    public LeaderboardCard(Context context) {
        super(context);
        rootView= LayoutInflater.from(context).inflate(R.layout.layout_leaderboard_card, this,true);
    }
    public LeaderboardCard(Context context, AttributeSet attrs){
        super(context,attrs);
        rootView=LayoutInflater.from(context).inflate(R.layout.layout_leaderboard_card, this,true);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.LeaderboardCard);
        if(typedArray!=null){
            int image = typedArray.getResourceId(R.styleable.LeaderboardCard_leaderboard_card_image,0);
            String name = typedArray.getString(R.styleable.LeaderboardCard_leaderboard_card_name);
            String score = typedArray.getString(R.styleable.LeaderboardCard_leaderboard_card_score);
            String num = typedArray.getString(R.styleable.LeaderboardCard_leaderboard_card_num);
            setAttr(image,name,score,num);
        }
    }
    public void setAttr(int image,String name,String score,String num){
        leaderboardImage =(RoundImageView)rootView.findViewById(R.id.activity_leaderboard_image);
        leaderboardName=(TextView)rootView.findViewById(R.id.activity_leaderboard_name);
        leaderboardScore=(TextView)rootView.findViewById(R.id.activity_leaderboard_score);
        leaderboardNum = (TextView)rootView.findViewById(R.id.activity_leaderboard_num) ;
        leaderboardImage.setImageResource(image);
        leaderboardName.setText(name);
        leaderboardScore.setText(score);
        leaderboardNum.setText(num);
        leaderboardName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }
    public RoundImageView getImage(){
        leaderboardImage = (RoundImageView) rootView.findViewById(R.id.activity_leaderboard_image);
        return leaderboardImage;
    }
    public TextView getName(){
        leaderboardName = (TextView) rootView.findViewById(R.id.activity_leaderboard_name);
        return leaderboardName;
    }
    public TextView getScore(){
        leaderboardScore = (TextView) rootView.findViewById(R.id.activity_leaderboard_score);
        return leaderboardScore;
    }
    public TextView getNum(){
        leaderboardNum =(TextView) rootView.findViewById(R.id.activity_leaderboard_num);
        return leaderboardNum;
    }
}
