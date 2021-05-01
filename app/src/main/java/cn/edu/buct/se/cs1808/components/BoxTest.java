package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;

public class BoxTest extends RelativeLayout {
    private RoundImageView boxImage;
    private ImageView boxGrade;
    private TextView boxName;
    private TextView boxScore;
    private View rootView;
    public BoxTest(Context context) {
        super(context);
        rootView=LayoutInflater.from(context).inflate(R.layout.layout_test_box, this,true);
    }
    public BoxTest(Context context, AttributeSet attrs){
        super(context,attrs);
        rootView=LayoutInflater.from(context).inflate(R.layout.layout_test_box, this,true);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.TestBox);
        if(typedArray!=null){
            int image = typedArray.getResourceId(R.styleable.TestBox_box_image,0);
            int grade = typedArray.getInt(R.styleable.TestBox_box_position,4);
            String name = typedArray.getString(R.styleable.TestBox_box_name);
            String score = typedArray.getString(R.styleable.TestBox_box_score);
            if(image==0){
                image = R.drawable.bleafumb_main_1;
            }
            setAttr(image,name,grade,score);
        }

    }
    public void setAttr(int image,String name,int grade,String score){
        boxImage = (RoundImageView) rootView.findViewById(R.id.test_img);
        boxGrade = (ImageView) rootView.findViewById(R.id.test_grade);
        boxName = (TextView) rootView.findViewById(R.id.test_name);
        boxScore = (TextView) rootView.findViewById(R.id.test_score);

        boxImage.setImageResource(image);
        if(grade==1){
            boxGrade.setBackgroundResource(R.drawable.common_gradeone);
        }
        else if(grade==2){
            boxGrade.setBackgroundResource(R.drawable.common_gradetwo);
        }
        else if(grade==3){
            boxGrade.setBackgroundResource(R.drawable.common_gradethree);
        }
        else{
            boxGrade.setVisibility(View.GONE);
        }
        boxName.setText(name);
        boxScore.setText(score);
        boxScore.setTextColor(android.graphics.Color.parseColor("#ee7712"));
        boxName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }
}

