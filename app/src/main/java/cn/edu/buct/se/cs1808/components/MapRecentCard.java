package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.baidu.mapapi.model.LatLng;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class MapRecentCard extends ConstraintLayout {
    private View view;
    private ImageView imageView;
    private TextView museumName;
    private TextView museumPos;
    private TextView museumInfo;
    private int museumId;
    private LatLng latLngPos;
    private static final String DEFAULT_NAME;
    private static final String DEFAULT_POS;
    private static final int DEFAULT_IMAGE;
    static {
        DEFAULT_NAME = "博物馆名称";
        DEFAULT_POS = "北京市";
        DEFAULT_IMAGE = R.mipmap.ic_launcher;
    }
    public MapRecentCard(Context context) {
        super(context);
        init(context);
    }
    public MapRecentCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MapRecentCard);
        if (typedArray != null) {
            String attrName = typedArray.getString(R.styleable.MapRecentCard_map_museum_name);
            String attrPos = typedArray.getString(R.styleable.MapRecentCard_map_museum_position);
            String attrInfo = typedArray.getString(R.styleable.MapRecentCard_map_museum_info);
            int attrImage = typedArray.getInt(R.styleable.MapRecentCard_map_museum_image, DEFAULT_IMAGE);
            setAttr(-1, attrName, attrPos, attrInfo, attrImage, null);
        }
    }
    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_map_recent, this);
        imageView = (ImageView) view.findViewById(R.id.mapMuseumImage);
        RoundView.setRadiusWithDp(10, imageView);
        museumName = (TextView) view.findViewById(R.id.mapMesumName);
        museumPos = (TextView) view.findViewById(R.id.mapPositionCity);
        museumInfo = (TextView) view.findViewById(R.id.mapMesumInfo);
    }

    public void setAttr(int id, String name, String pos, String info, int imageResourceId, LatLng latLng) {
        if (name == null) name = DEFAULT_NAME;
        if (pos == null) pos = DEFAULT_POS;
        imageView.setImageResource(imageResourceId);
        museumName.setText(name);
        museumPos.setText(pos);
        museumInfo.setText(info);
        latLngPos = latLng;
        museumId = id;
    }

    public void setAttr(int id, String name, String pos, String info, Bitmap image, LatLng latLng) {
        if (name == null) name = DEFAULT_NAME;
        if (pos == null) pos = DEFAULT_POS;
        imageView.setImageBitmap(image);
        museumName.setText(name);
        museumPos.setText(pos);
        museumInfo.setText(info);
        latLngPos = latLng;
        museumId = id;
    }

    public void setAttr(int id, String name, String pos, String info, String imageUrl, LatLng latLng) {
        setAttr(id, name, pos, info, DEFAULT_IMAGE, latLng);
        LoadImage loadImage = new LoadImage(imageView);
        loadImage.setBitmap(imageUrl);
    }

    public String getMuseumName() {
        if (museumName == null) return null;
        return museumName.getText().toString();
    }

    public LatLng getLatLngPos() {
        return latLngPos;
    }

    public int getMuseumId() {
        return museumId;
    }
}
