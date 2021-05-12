package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import cn.edu.buct.se.cs1808.R;

public class TextWithIcon extends ConstraintLayout {
    private View view;
    private ImageView icon;
    private TextView text;
    public TextWithIcon(Context context) {
        super(context);
        init(context);
    }

    public TextWithIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextWithIcon);
        if (typedArray != null) {
            String text = typedArray.getString(R.styleable.TextWithIcon_text_value);
            int imageId = typedArray.getResourceId(R.styleable.TextWithIcon_icon_image, R.drawable.essay_mine_page_listitem_icon);
            int color = typedArray.getColor(R.styleable.TextWithIcon_text_color, getResources().getColor(R.color.white));
            setAttr(text, imageId, color);
        }
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_text_with_icon, this);
        icon = (ImageView) view.findViewById(R.id.iconImage);
        text = (TextView) view.findViewById(R.id.textValue);
    }

    public void setAttr(String value, int image, int color) {
        icon.setImageResource(image);
        text.setText(value);
        text.setTextColor(color);
    }
}
