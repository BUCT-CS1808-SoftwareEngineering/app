package cn.edu.buct.se.cs1808.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

import java.util.Objects;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;
import cn.edu.buct.se.cs1808.components.MapRecentCard;
import cn.edu.buct.se.cs1808.utils.Permission;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class MapFragmentNav extends NavBaseFragment {
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient locationClient;

    private LinearLayout cardsView;
    private RoundImageView searchButton;
    private EditText searchInput;

    public MapFragmentNav() {
        activityId = R.layout.activity_map;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMap();
        // 定位时跳转到当前位置
        getLocationAndJump = true;
        // 启用定位
        startLocation();

        cardsView = (LinearLayout) findViewById(R.id.mapCardsView);
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

    /**
     * 初始化地图
     */
    private void initMap() {
        mapView = (MapView) findViewById(R.id.bmapView);
        RoundView.setRadius(24, mapView);
        // 禁止现实缩放按钮
        mapView.showZoomControls(false);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
        locationClient = new LocationClient(ctx);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        locationClient.setLocOption(option);
        MapLocationListener listener = new MapLocationListener();
        locationClient.registerLocationListener(listener);
        UiSettings uiSettings = baiduMap.getUiSettings();
        // 禁止手动旋转
        uiSettings.setRotateGesturesEnabled(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startLocation() {
        if (!Permission.check(ctx, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Permission.request(this, Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }
        locationClient.start();
    }

    /**
     * 权限申请回调
     * @param requestCode requestCode
     * @param permissions 权限列表
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == Permission.REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i ++) {
                if (Objects.equals(permissions[i], Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        locationClient.start();
                    }
                    else {
                        Toast.makeText(ctx, "请开启定位权限，否则无法进行定位!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * 地图视角跳转到上一次定位的位置
     */
    private void gotoLastLocation() {
        if (lastBDLocation == null) return;
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(lastBDLocation.getLatitude(), lastBDLocation.getLongitude())));
        // 设置默认的缩放级别
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(16.0f);
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }


    @Override
    public int getItemId() {
        return R.id.navigation_map;
    }

    private BDLocation lastBDLocation;
    private boolean getLocationAndJump = false;
    private class MapLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (mapView == null || baiduMap == null || bdLocation == null) {
                return;
            }
            lastBDLocation = bdLocation;
            Log.i("Get Location", bdLocation.toString());
            MyLocationData locationData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(bdLocation.getDirection())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            baiduMap.setMyLocationData(locationData);
            if (getLocationAndJump) {
                gotoLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        locationClient.stop();
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        super.onDestroy();
    }
}
