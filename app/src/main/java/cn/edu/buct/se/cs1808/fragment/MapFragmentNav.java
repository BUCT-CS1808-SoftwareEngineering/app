package cn.edu.buct.se.cs1808.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.HashMap;
import java.util.Objects;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;
import cn.edu.buct.se.cs1808.components.MapRecentCard;
import cn.edu.buct.se.cs1808.utils.BitmapUtil;
import cn.edu.buct.se.cs1808.utils.Permission;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class MapFragmentNav extends NavBaseFragment {
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private final int locationScanPan = 0;
    private HashMap<MarkerWithEquals, Integer> allMarkers;

    private LinearLayout cardsView;
    private EditText searchInput;

    public MapFragmentNav() {
        activityId = R.layout.activity_map;
        allMarkers = new HashMap<>();
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
        RoundImageView searchButton = (RoundImageView) findViewById(R.id.mapSearchButton);
        searchInput = (EditText) findViewById(R.id.mapSearchInput);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = searchInput.getText().toString();
                search(q);
            }
        });
        // marker  点击事件
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                gotoPosition(marker.getPosition(), 16);
                Log.i("Marker id", String.valueOf(allMarkers.get(new MarkerWithEquals(marker))));
                return false;
            }
        });

        addCards("北京故宫博物院", "北京市", "北京故宫博物馆", "http://7q5evw.com1.z0.glb.clouddn.com/images/article/FtPbcYX5VeTM6CfEBsCVi2aGRj0n.jpg");
        addCards("国家博物馆", "北京市", "国家博物馆", "https://pic.baike.soso.com/ugc/baikepic2/26022/cut-20190829122815-1940041223_jpg_751_600_36257.jpg/300");
        addCards("陕西历史博物馆", "西安市", "陕西历史博物馆", "http://5b0988e595225.cdn.sohucs.com/images/20200512/035c683a24a3421fafdd1515e2c73e93.jpeg");

        addMark(1,  116.403945, 39.914036);
        addMark(2, 116.282821, 39.902553);
        removeAllMarkers();
    }

    /**
     * 添加最近浏览卡片
     * @param name 博物馆名称
     * @param pos 博物馆地点
     * @param info 博物馆信息
     * @param imageurl 博物馆图片地址
     */
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
        option.setScanSpan(locationScanPan);
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
        gotoPosition(lastBDLocation.getLongitude(), lastBDLocation.getLatitude(), 16);
    }

    /**
     * 地图视角跳转到指定位置并缩放
     * @param longitude 经度
     * @param latitude 纬度
     * @param zoom 缩放级别
     */
    private void gotoPosition(double longitude, double latitude, float zoom) {
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        // 设置默认的缩放级别
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(zoom);
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * 地图视角跳转到指定位置并缩放
     * @param latLng 坐标对象
     * @param zoom 缩放级别
     */
    private void gotoPosition(LatLng latLng, float zoom) {
        gotoPosition(latLng.longitude, latLng.latitude, zoom);
    }

    /**
     * 添加一个标记到地图上
     * @param id 标记的ID信息，其对应博物馆的ID
     * @param longitude 经度
     * @param latitude 纬度
     */
    private void addMark(int id, double longitude, double latitude) {
        LatLng latLng = new LatLng(latitude, longitude);
        Bitmap bitmap = BitmapUtil.zoomImg(BitmapFactory.decodeResource(getResources(), R.drawable.essay_positioning), 88, 88);
        BitmapDescriptor mapBitmap = BitmapDescriptorFactory.fromBitmap(bitmap);
        OverlayOptions option = new MarkerOptions().position(latLng).icon(mapBitmap);
        Marker marker = (Marker) baiduMap.addOverlay(option);
        MarkerWithEquals markerWithEquals = new MarkerWithEquals(marker);
        if (allMarkers.containsKey(markerWithEquals)) {
            markerWithEquals.marker.remove();
        }
        allMarkers.put(markerWithEquals, id);
    }

    /**
     * 移除所有的地图标记
     */
    private void removeAllMarkers() {
        for (MarkerWithEquals marker : allMarkers.keySet()) {
            marker.marker.remove();
        }
        allMarkers.clear();
    }


    @Override
    public int getItemId() {
        return R.id.navigation_map;
    }

    private BDLocation lastBDLocation;
    private boolean getLocationAndJump = false;

    /**
     * 地图定位回调监听器类
     */
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
            if (locationScanPan == 0) {
                locationClient.stop();
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

    /**
     * 为了解决Marker类无法通过经纬度相等判断相等，无法作为map的key
     * 内部使用经纬度判断equals以及计算hashCode
     */
    private class MarkerWithEquals {
        public Marker marker;
        MarkerWithEquals(Marker marker) {
            this.marker = marker;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MarkerWithEquals that = (MarkerWithEquals) o;
            LatLng a = marker.getPosition();
            LatLng b = that.marker.getPosition();
            return doubleEquals(a.latitude, b.latitude) && doubleEquals(a.longitude, b.longitude);
        }

        @Override
        public int hashCode() {
            LatLng a = marker.getPosition();
            int res = 17;
            res = res * 31 + (int) a.longitude;
            res = res * 31 + (int) a.latitude;
            return res;
        }

        private boolean doubleEquals(double a, double b) {
            return Math.abs(a - b) < 0.00001d;
        }
    }
}
