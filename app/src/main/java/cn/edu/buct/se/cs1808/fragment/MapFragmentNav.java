package cn.edu.buct.se.cs1808.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baidu.mapapi.map.MapView;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.components.MapRecentCard;
import cn.edu.buct.se.cs1808.utils.LoadImage;

public class MapFragmentNav extends NavBaseFragment {
    private MapView mapView = null;
    private LinearLayout cardsView;

    public MapFragmentNav() {
        activityId = R.layout.activity_map;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) findViewById(R.id.bmapView);
        cardsView = (LinearLayout) findViewById(R.id.mapCardsView);

        addCards("北京故宫博物院", "北京市", "http://7q5evw.com1.z0.glb.clouddn.com/images/article/FtPbcYX5VeTM6CfEBsCVi2aGRj0n.jpg");
        addCards("国家博物馆", "北京市", "http://7q5evw.com1.z0.glb.clouddn.com/images/article/FtPbcYX5VeTM6CfEBsCVi2aGRj0n.jpg");
    }

    private void addCards(String name, String pos, String imageurl) {
        MapRecentCard mapRecentCard = new MapRecentCard(ctx);
        mapRecentCard.setAttr(name, pos, imageurl);
        cardsView.addView(mapRecentCard);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mapRecentCard.getLayoutParams();
        lp.setMarginEnd(16);
        mapRecentCard.setLayoutParams(lp);
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
