package cn.edu.buct.se.cs1808.fragment;

import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baidu.mapapi.map.MapView;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;
import cn.edu.buct.se.cs1808.components.MapRecentCard;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class MapFragmentNav extends NavBaseFragment {
    private MapView mapView = null;
    private LinearLayout cardsView;
    private RoundImageView searchButton;
    private EditText searchInput;

    public MapFragmentNav() {
        activityId = R.layout.activity_map;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) findViewById(R.id.bmapView);
        cardsView = (LinearLayout) findViewById(R.id.mapCardsView);
        RoundView.setRadius(24, mapView);

        // 搜索按钮点击事件
        searchButton = (RoundImageView) findViewById(R.id.mapSearchButton);
        searchInput = (EditText) findViewById(R.id.mapSearchInput);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = searchInput.getText().toString();
                search(q);
            }
        });

        addCards("北京故宫博物院", "北京市", "北京故宫博物馆", "http://7q5evw.com1.z0.glb.clouddn.com/images/article/FtPbcYX5VeTM6CfEBsCVi2aGRj0n.jpg");
        addCards("国家博物馆", "北京市", "国家博物馆", "https://pic.baike.soso.com/ugc/baikepic2/26022/cut-20190829122815-1940041223_jpg_751_600_36257.jpg/300");
        addCards("陕西历史博物馆", "西安市", "陕西历史博物馆", "http://5b0988e595225.cdn.sohucs.com/images/20200512/035c683a24a3421fafdd1515e2c73e93.jpeg");
    }

    private void addCards(String name, String pos, String info, String imageurl) {
        MapRecentCard mapRecentCard = new MapRecentCard(ctx);
        mapRecentCard.setAttr(name, pos, info, imageurl);
        cardsView.addView(mapRecentCard);
        RoundView.setRadius(24, mapRecentCard);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mapRecentCard.getLayoutParams();
        lp.setMarginEnd(32);
        mapRecentCard.setLayoutParams(lp);
        // 添加卡片点击事件
        mapRecentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardOnClick(mapRecentCard);
            }
        });
    }

    private void search(String q) {
        if (q == null || q.length() == 0) return;
        Log.i("Map Search", q);
    }
    private void cardOnClick(MapRecentCard mapRecentCard) {
        if (mapRecentCard == null) return;
        Log.i("Card Click", mapRecentCard.getMuseumName());
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public int getItemId() {
        return R.id.navigation_map;
    }
}
