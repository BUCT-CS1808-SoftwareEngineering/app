package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.List;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.utils.DensityUtil;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class MinePageList extends LinearLayout {
    LinearLayout itemsArea;
    TextView title;
    public MinePageList(Context context) {
        super(context);
        init(context);
    }
    public MinePageList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MinePageList);
        if (typedArray != null) {
            String title = typedArray.getString(R.styleable.MinePageList_list_name);
            setTitle(title);
            int radius = typedArray.getInt(R.styleable.MinePageList_card_radius, 0);
            RoundView.setRadiusWithDp(radius, this);
        }
    }

    public MinePageList(Context context, List<MinePageListItem> items) {
        super(context);
        init(context);
        addItems(items);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_mine_page_list, this);
        itemsArea = (LinearLayout) view.findViewById(R.id.itemsArea);
        title = (TextView) view.findViewById(R.id.minePageCardTitle);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof MinePageListItem) {
            addItem((MinePageListItem) child);
        }
        else {
            super.addView(child, index, params);
        }
    }

    public void addItems(List<MinePageListItem> items) {
        if (items == null) return;
        for (MinePageListItem item : items) {
            addItem(item);
        }
    }
    public void addItem(MinePageListItem item) {
        item.setPadding(0, 0, 0, DensityUtil.dip2px(getContext(), 8));
        itemsArea.addView(item);
    }
    public void setTitle(String title) {
        if (title == null) return;
        this.title.setText(title);
    }

}
