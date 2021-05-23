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
import cn.edu.buct.se.cs1808.utils.LoadImage;

public class MuseumCard extends LinearLayout {
    private RoundImageView museumImage;
    private TextView museumText;
    private TextView museumName;
    private TextView museumScore;
    private View rootView;
    private int textCutLen=55;
    public MuseumCard(Context context) {
        super(context);
        rootView=LayoutInflater.from(context).inflate(R.layout.layout_museum_card, this,true);
    }
    public MuseumCard(Context context, AttributeSet attrs){
        super(context,attrs);
        rootView=LayoutInflater.from(context).inflate(R.layout.layout_museum_card, this,true);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.MuseumCard);
        if(typedArray!=null){
            int image = typedArray.getResourceId(R.styleable.MuseumCard_museum_card_image,0);
            String name = typedArray.getString(R.styleable.MuseumCard_museum_card_name);
            String score = typedArray.getString(R.styleable.MuseumCard_museum_card_score);
            String text = typedArray.getString(R.styleable.MuseumCard_museum_card_text);
            setAttr("",name,text,score);
        }
    }
    public void setAttr(String image,String name,String text,String score){
        int len = text.length();
        if(len>=textCutLen){
            text = text.substring(0,textCutLen);
            text+="...";
        }
        museumImage = (RoundImageView) rootView.findViewById(R.id.museum_card_image);
        museumName = (TextView) rootView.findViewById(R.id.museum_card_name);
        museumScore = (TextView) rootView.findViewById(R.id.museum_card_score);
        museumText = (TextView) rootView.findViewById(R.id.museum_card_text);

        if(image.length()==0){
            museumImage.setImageResource(R.drawable.bleafumb_main_3);
        }
        LoadImage loader = new LoadImage(museumImage);
        loader.setBitmap(image);
        museumText.setText(text);
        museumScore.setText(score);
        museumName.setText(name);
        museumName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        museumScore.setTextColor(android.graphics.Color.parseColor("#ee7712"));
    }
    public RoundImageView getMuseumImage(){
        museumImage = (RoundImageView) rootView.findViewById(R.id.museum_card_image);
        return museumImage;
    }
    public ImageView getThreePoint(){
        ImageView res = (ImageView) rootView.findViewById(R.id.museum_card_menu);
        return res;
    }
    public TextView getMuseumName(){
        museumText = (TextView) rootView.findViewById(R.id.museum_card_name);
        return museumText;
    }
}
