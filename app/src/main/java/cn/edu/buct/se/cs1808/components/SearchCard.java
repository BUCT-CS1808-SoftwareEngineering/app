package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;

public class SearchCard extends RelativeLayout {
    private View rootView;
    private RoundImageView searchImage;
    private TextView searchName;
    private TextView searchType;
    public SearchCard(Context context) {
        super(context);
        rootView= LayoutInflater.from(context).inflate(R.layout.layout_search_card, this,true);
    }
    public SearchCard(Context context, AttributeSet attrs){
        super(context,attrs);
        rootView= LayoutInflater.from(context).inflate(R.layout.layout_search_card, this,true);
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.SearchCard);
        if(typedArray!=null){
            int image = typedArray.getResourceId(R.styleable.SearchCard_search_image,0);
            String name = typedArray.getString(R.styleable.SearchCard_search_name);
            String type = typedArray.getString(R.styleable.SearchCard_search_type);
            setAttr(image,name,type);
        }
    }
    public void setAttr(int image,String name,String type){

        searchImage = (RoundImageView) rootView.findViewById(R.id.search_card_image);
        searchName = (TextView) rootView.findViewById(R.id.search_card_name);
        searchType = (TextView) rootView.findViewById(R.id.search_card_type);

        searchImage.setImageResource(image);
        searchName.setText(name);
        searchType.setText(type);
    }
}
