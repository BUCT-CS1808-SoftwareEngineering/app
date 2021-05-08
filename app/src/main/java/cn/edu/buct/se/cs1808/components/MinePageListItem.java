package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import cn.edu.buct.se.cs1808.R;

public class MinePageListItem extends ConstraintLayout {
    ImageView icon;
    TextView content;
    Class<?> target;
    public MinePageListItem(Context context) {
        super(context);
        init(context);
    }
    public MinePageListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MinePageListItem);
        if (typedArray != null) {
            String content = typedArray.getString(R.styleable.MinePageListItem_item_content);
            setContent(content);
            int resid = typedArray.getResourceId(R.styleable.MinePageListItem_item_icon, R.drawable.essay_mine_page_listitem_icon);
            setIcon(resid);
            String target = typedArray.getString(R.styleable.MinePageListItem_jump_target);
            if (target != null && target.length() > 0) {
                try {
                    Class<?> targetClass = Class.forName(target);
                    setTarget(targetClass);
                }
                catch (ClassNotFoundException e) {
                    this.target = null;
                }
            }
        }
    }

    public MinePageListItem(Context context, int resid, String content, Class<?> target) {
        super(context);
        init(context);
        setContent(content);
        setIcon(resid);
        setTarget(target);
    }
    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_mine_page_list_item, this);
        icon = (ImageView) findViewById(R.id.itemIcon);
        content = (TextView) findViewById(R.id.itemContent);
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.itemLayout);
        layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (target == null) return;
                Intent intent = new Intent(context, target);
                context.startActivity(intent);
            }
        });
    }


    public void setContent(String content) {
         this.content.setText(content);
    }
    public void setIcon(int resourceId) {
        this.icon.setImageResource(resourceId);
    }
    public void setTarget(Class<?> target) {
        this.target = target;
    }
}
