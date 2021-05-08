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

public class ExhibitionCard extends LinearLayout {
    private RoundImageView exhibitionImage;
    private TextView exhibitionName;
    private View rootView;
    private int textCutLen=55;
    public ExhibitionCard(Context context) {
        super(context);
        rootView= LayoutInflater.from(context).inflate(R.layout.layout_exhibition_card, this,true);
    }
    public ExhibitionCard(Context context, AttributeSet attrs){
        super(context,attrs);
        rootView=LayoutInflater.from(context).inflate(R.layout.layout_exhibition_card, this,true);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.ExhibitionCard);
        if(typedArray!=null){
            int image = typedArray.getResourceId(R.styleable.ExhibitionCard_exhibition_card_image,0);
            String name = typedArray.getString(R.styleable.ExhibitionCard_exhibition_card_name);
            setAttr(image,name);
        }
    }
    public void setAttr(int image,String name){
        exhibitionImage =(RoundImageView)rootView.findViewById(R.id.exhibition_card_image);
        exhibitionName=(TextView)rootView.findViewById(R.id.exhibition_card_name);
        exhibitionImage.setImageResource(image);
        exhibitionName.setText(name);
        exhibitionName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }
    public RoundImageView getMuseumImage(){
        exhibitionImage = (RoundImageView) rootView.findViewById(R.id.exhibition_card_image);
        return exhibitionImage;
    }
    public TextView getMuseumName(){
        exhibitionName = (TextView) rootView.findViewById(R.id.exhibition_card_name);
        return exhibitionName;
    }
}
