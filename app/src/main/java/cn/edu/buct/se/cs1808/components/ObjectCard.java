package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.utils.LoadImage;

public class ObjectCard extends LinearLayout {
    private RoundImageView objectImage;
    private TextView objectName;
    private View rootView;
    private int textCutLen=55;
    public ObjectCard(Context context) {
        super(context);
        rootView= LayoutInflater.from(context).inflate(R.layout.layout_object_card, this,true);
    }
    public ObjectCard(Context context, AttributeSet attrs){
        super(context,attrs);
        rootView=LayoutInflater.from(context).inflate(R.layout.layout_object_card, this,true);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.ObjectCard);
        if(typedArray!=null){
                int image = typedArray.getResourceId(R.styleable.ObjectCard_object_card_image,0);
            String name = typedArray.getString(R.styleable.ObjectCard_object_card_name);
            setAttr(image,name);
        }
    }
    public void setAttr(int image,String name){
        objectImage =(RoundImageView)rootView.findViewById(R.id.object_card_image);
        objectName=(TextView)rootView.findViewById(R.id.object_card_name);
        objectImage.setImageResource(image);
        objectName.setText(name);
        objectName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }
    public void setAttr(String image,String name){
        objectImage =(RoundImageView)rootView.findViewById(R.id.object_card_image);
        objectName=(TextView)rootView.findViewById(R.id.object_card_name);
        if(image.length()==0){
            objectImage.setImageResource(R.drawable.bleafumb_main_3);
        }
        LoadImage loader = new LoadImage(objectImage);
        loader.setBitmap(image);
        objectName.setText(name);
        objectName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
    }
    public RoundImageView getMuseumImage(){
        objectImage = (RoundImageView) rootView.findViewById(R.id.object_card_image);
        return objectImage;
    }
    public TextView getMuseumName(){
        objectName = (TextView) rootView.findViewById(R.id.object_card_name);
        return objectName;
    }
}
