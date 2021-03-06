package cn.edu.buct.se.cs1808.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import cn.edu.buct.se.cs1808.MuseumActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.components.RoundImageView;
import cn.edu.buct.se.cs1808.VideoIntroduceActivity;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.MapRecentCard;
import cn.edu.buct.se.cs1808.utils.BitmapUtil;
import cn.edu.buct.se.cs1808.utils.DensityUtil;
import cn.edu.buct.se.cs1808.utils.JsonFileHandler;
import cn.edu.buct.se.cs1808.utils.Museum;
import cn.edu.buct.se.cs1808.utils.JSONArraySort;
import cn.edu.buct.se.cs1808.utils.Permission;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class MapFragmentNav extends NavBaseFragment {
    private MapView mapView;
    private BaiduMap baiduMap;
    private LocationClient locationClient;
    private final int locationScanPan = 0;
    private HashMap<MarkerWithEquals, Integer> allMarkers;
    private HashMap<Integer, Museum> allMuseums;
    private RoutePlanSearch mSearch;

    private LinearLayout cardsView;
    private EditText searchInput;

    private final String CARD_FILENAME;


    private Museum currentMuseum;
    private Marker currentMarker;
    private MapMuseumCard bottomCard;
    private int MAX_RECENT_CARDS = 9;

    public MapFragmentNav() {
        activityId = R.layout.activity_map;
        allMarkers = new HashMap<>();
        allMuseums = new HashMap<>();
        CARD_FILENAME = "recent_view.json";
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMap();
        startLocation();
        // ??????????????????????????????
        getLocationAndJump = true;
        cardsView = (LinearLayout) findViewById(R.id.mapCardsView);
        // ????????????????????????
        RoundImageView searchButton = (RoundImageView) findViewById(R.id.mapSearchButton);
        searchInput = (EditText) findViewById(R.id.mapSearchInput);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = searchInput.getText().toString();
                search(q);
            }
        });
        // marker  ????????????
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                currentMarker = marker;
                gotoPosition(marker.getPosition(), 18f);
//                getDrivingRouterLine(new LatLng(lastBDLocation.getLatitude(), lastBDLocation.getLongitude()), marker.getPosition());
                int id = allMarkers.get(new MarkerWithEquals(marker));
                Log.i("Marker id", String.valueOf(id));
                // ??????????????????
                Museum museum = allMuseums.get(id);
                currentMuseum = museum;
                LatLng lastLocation = new LatLng(lastBDLocation.getLatitude(), lastBDLocation.getLongitude());
                double distance = getDistance(lastLocation, marker.getPosition());
                Log.i("Distance", String.valueOf(distance));
                initBottomCard(museum, String.format("%.3f", distance));
                bottomCard.show(getFragmentManager(), "??????");
                bottomCard.loadMuseumVideo(ctx, id, 5);
                bottomCard.loadMuseumExhibitions(ctx, id, 5);
                return false;
            }
        });

        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.horizontalScrollView);
        recentViewTitle = (TextView) view.findViewById(R.id.textViewMap);
        loadCards(MAX_RECENT_CARDS);
    }

    /**
     * ?????????????????????
     * @param museum ???????????????
     */
    private void initBottomCard(Museum museum, String distance) {
        if (bottomCard != null) {
            bottomCard.setMuseumInfo(museum, distance);
            return;
        }
        bottomCard = new MapMuseumCard(museum, distance);
        bottomCard.setOnClickListener(new MapMuseumCard.OnClickListener() {
            @Override
            public void onClick(MapMuseumCard.ClickAction action) {
                switch (action) {
                    case MORE_VIDEO_CLICK:
                        Log.i("Event", action.name());
                        Intent intent = new Intent(ctx, VideoIntroduceActivity.class);
                        intent.putExtra("muse_ID", currentMuseum.getId());
                        intent.putExtra("muse_Name", currentMuseum.getName());
                        startActivity(intent);
                        break;
                    case MORE_EXHIBITION_CLICK:
                        Log.i("Event", action.name());
                        Intent intent1 = new Intent(ctx, MuseumActivity.class);
                        intent1.putExtra("muse_ID", currentMuseum.getId());
                        intent1.putExtra("fragment_ID", 2);
                        startActivity(intent1);
                        break;
                    case MUSEUM_IMAGE_CLICK:
                        Log.i("Event", action.name());
                        Intent intent2 = new Intent(ctx, MuseumActivity.class);
                        intent2.putExtra("muse_ID", currentMuseum.getId());
                        intent2.putExtra("target", "exhibition");
                        startActivity(intent2);
                        break;
                    case GET_WALK_ROUTER_CLICK:
                        getWalkingRouterLines(new LatLng(lastBDLocation.getLatitude(), lastBDLocation.getLongitude()), currentMuseum.getLatLng());
                        gotoPosition(lastBDLocation.getLongitude(), lastBDLocation.getLatitude(), 11.5f);
                        bottomCard.dismiss();
                        Log.i("Event", action.name());
                        break;
                    case GET_DRIVE_ROUTER_CLICK:
                        getDrivingRouterLine(new LatLng(lastBDLocation.getLatitude(), lastBDLocation.getLongitude()), currentMuseum.getLatLng());
                        bottomCard.dismiss();
                        gotoPosition(lastBDLocation.getLongitude(), lastBDLocation.getLatitude(), 11.5f);
                        Log.i("Event", action.name());
                        break;
                    default:
                        break;
                }
                LatLng pos = currentMuseum.getLatLng();
                saveRecentCard(currentMuseum.getId(), currentMuseum.getName(), currentMuseum.getPos(), currentMuseum.getIntroduce(), currentMuseum.getImageSrc(), pos);
                cardsView.removeAllViews();
                loadCards(MAX_RECENT_CARDS);
            }
        });
    }

    /**
     * ???????????????????????????
     * @param start ?????????
     * @param end ??????
     * @return ??????
     */
    private double getDistance(LatLng start, LatLng end) {
        double lon1 = (Math.PI / 180) * start.longitude;
        double lon2 = (Math.PI / 180) * end.longitude;
        double lat1 = (Math.PI / 180) * start.latitude;
        double lat2 = (Math.PI / 180) * end.latitude;

        double a = lat1 - lat2;
        double b = lon1 - lon2;
        // ??????????????? km?????????????????????????????????*1000????????????
        return 2 * Math.asin(Math.sqrt(Math.sin(a / 2) * Math.sin(a / 2) + Math.cos(lat1) * Math.cos(lat2) * Math.sin(b / 2) * Math.sin(b / 2))) * 6378.137;
    }


    private HorizontalScrollView horizontalScrollView;
    private TextView recentViewTitle;
    /**
     * ??????????????????????????????????????????
     * @param ifShow ????????????
     */
    private void hiddenRecentArea(boolean ifShow) {
        if (ifShow) {
            horizontalScrollView.setVisibility(View.VISIBLE);
            recentViewTitle.setVisibility(View.VISIBLE);
        }
        else {
            horizontalScrollView.setVisibility(View.GONE);
            recentViewTitle.setVisibility(View.GONE);
        }
    }
    /**
     * ????????????????????????????????????????????????
     * @return ?????????????????????
     */
    private int loadCards(int maxNums) {
        JSONArray allCards = JsonFileHandler.readJsonArray(ctx, CARD_FILENAME);
        if (allCards == null) {
            hiddenRecentArea(false);
            return 0;
        }
        int res = 0;
        boolean showFlag = false;
        for (int i = allCards.length() - 1; i >= 0 && maxNums > 0; i --) {
            try {
                JSONObject item = allCards.getJSONObject(i);
                String name = item.getString("name");
                String pos = item.getString("pos");
                String info = item.getString("info");
                String imgSrc = item.getString("img_src");
                double latitude = item.getDouble("latitude");
                double longitude = item.getDouble("longitude");
                int id = item.getInt("id");
                addCards(id, name, pos, info, imgSrc, new LatLng(latitude, longitude));
                showFlag = true;
                maxNums --;
            }
            catch (JSONException e) {
                continue;
            }
            res ++;
        }
        hiddenRecentArea(showFlag);
        return res;
    }

    /**
     * ????????????????????????????????????????????????
     * @param id ??????????????????ID
     * @param name ???????????????
     * @param posCity ????????????????????????
     * @param introduce ???????????????
     * @param imageSrc ?????????????????????
     * @param pos ???????????????
     * @return ??????????????????
     */
    private boolean saveRecentCard(int id, String name, String posCity, String introduce, String imageSrc, LatLng pos) {
        JSONArray allCards = JsonFileHandler.readJsonArray(ctx, CARD_FILENAME);
        if (allCards == null) {
            allCards = new JSONArray();
        }
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", name);
            jsonObject.put("pos", posCity);
            jsonObject.put("info", introduce);
            jsonObject.put("img_src", imageSrc);
            jsonObject.put("latitude", pos.latitude);
            jsonObject.put("longitude", pos.longitude);
            jsonObject.put("id", id);
        }
        catch (JSONException ignore) {
            return false;
        }
        boolean flag = true;
        int itemIndex = -1;
        // ??????
        for (int i = 0; i < MAX_RECENT_CARDS; i ++) {
            try {
                JSONObject item = allCards.getJSONObject(i);
                int itemId = item.getInt("id");
                if (itemId == id) {
                    flag = false;
                    itemIndex = i;
                    break;
                }
            }
            catch (JSONException ignore) {
            }
        }
        if (flag) {
            allCards.put(jsonObject);
        }
        else {
            // ?????????????????????????????????????????????(????????????????????????)
            try {
                JSONObject b = allCards.getJSONObject(itemIndex);
                allCards.remove(itemIndex);
                allCards.put(b);
            }
            catch (JSONException ignore) {}
        }
        for (int i = allCards.length() - 1 - MAX_RECENT_CARDS; i >= 0; i --) {
            allCards.remove(i);
        }
        return JsonFileHandler.write(ctx, CARD_FILENAME, allCards.toString(), StandardCharsets.UTF_8, Context.MODE_PRIVATE);
    }
    /**
     * ????????????????????????
     * @param name ???????????????
     * @param pos ???????????????
     * @param info ???????????????
     * @param imageUrl ?????????????????????
     */
    private void addCards(int id, String name, String pos, String info, String imageUrl, LatLng latLng) {
        MapRecentCard mapRecentCard = new MapRecentCard(ctx);
        // ??????????????????????????????????????????
        int maxLength = 43;
        if (info.length() > maxLength) {
            info = info.substring(0, maxLength) + "??????";
        }
        mapRecentCard.setAttr(id, name, pos, info, imageUrl, latLng);
        cardsView.addView(mapRecentCard);
        RoundView.setRadius(24, mapRecentCard);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mapRecentCard.getLayoutParams();
        lp.setMarginEnd(32);
        lp.width = DensityUtil.dip2px(ctx, 250);
        mapRecentCard.setLayoutParams(lp);
        // ????????????????????????
        mapRecentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardOnClick(mapRecentCard);
            }
        });
    }

    /**
     * ???????????????????????????, ??????????????????
     * @param q ???????????????
     */
    private void search(String q) {
        Log.i("Map Search", q);
        // ?????????????????????????????????????????????????????????????????????marker????????????
        removeAllMarkers();
        loadMuseum(q);
    }

    /**
     * ???????????????????????????
     * @param museums ???????????????
     */
    private void addMuseums(List<Museum> museums) {
        allMuseums.clear();
        if (museums != null) {
            if (museums.size() != 0) {
                Toast.makeText(ctx, String.format("??????%d???????????????", museums.size()), Toast.LENGTH_SHORT).show();
            }
            for (int i = 0; i < museums.size(); i ++) {
                Museum item = museums.get(i);
                allMuseums.put(item.getId(), item);
                addMark(item.getId(), item.getLatLng().longitude, item.getLatLng().latitude);
                if (i == 0) {
                    // ?????????????????????????????????????????????
                    gotoPosition(item.getLatLng(), 18f);
                }
            }
        }
    }

    /**
     * ????????????????????????
     */
    private void loadMuseum() {
        JSONObject params = new JSONObject();
        try {
            params.put("pageIndex", 1);
            params.put("pageSize", 300);
        }
        catch (JSONException e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        ApiTool.request(ctx, ApiPath.GET_ALL_MUSEUM_INFO, params, (JSONObject rep) -> {
            addMuseums(loadMuseumsFromResponse(rep, null));
        }, (JSONObject error) -> {
            try {
                Toast.makeText(ctx, error.getString("message"), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(ctx, "??????????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }
    /**
     * ???????????????????????????????????????????????????
     * @param name ???????????????
     */
    private void loadMuseum(String name) {
        if (name == null || name.length() == 0) {
            // ?????????????????????????????????????????????????????????????????????
            loadMuseum();
            return;
        }
        JSONObject params = new JSONObject();
        try {
            params.put("pageIndex", 1);
            params.put("pageSize", 300);
            // ??????????????????????????????????????????????????????????????????,???????????????????????????????????????
            String newName = name.replaceAll("??????(???|???)?", "");
            if (newName.length() == 0) {
                newName = name;
            }
            params.put("muse_Name", newName);
        }
        catch (JSONException e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        ApiTool.request(ctx, ApiPath.GET_ALL_MUSEUM_INFO, params, (JSONObject rep) -> {
            addMuseums(loadMuseumsFromResponse(rep, name));
        }, (JSONObject error) -> {
            try {
                Toast.makeText(ctx, error.getString("info"), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(ctx, "??????????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * ???????????????????????????????????????
     * @param rep ?????????????????????
     * @param sortName ??????????????????????????????null????????????
     * @return ???????????????
     */
    private List<Museum> loadMuseumsFromResponse(JSONObject rep, String sortName) {
        List<Museum> allMuseums = new ArrayList<>();
        if (rep == null) {
            Toast.makeText(ctx, "????????????", Toast.LENGTH_SHORT).show();
            return allMuseums;
        }
        String code;
        try {
            code = rep.getString("code");
        }
        catch (JSONException e) {
            code = null;
        }
        // ????????????code = success ?????????????????????????????????
        if (!"success".equals(code)) {
            Toast.makeText(ctx, "????????????: " + code, Toast.LENGTH_SHORT).show();
            return allMuseums;
        }
        try {
            JSONObject info = rep.getJSONObject("info");
            JSONArray data = info.getJSONArray("items");
            if (sortName != null) {
                data = JSONArraySort.sort(sortName, data);
            }
            if (data.length() == 0) {
                Toast.makeText(ctx, "?????????????????????", Toast.LENGTH_SHORT).show();
            }
            for (int i = 0; i < data.length(); i ++) {
                JSONObject item = data.getJSONObject(i);
                if (!item.has("muse_Name")) continue;
                String location = item.getString("muse_Location");
                String[] temp = location.split(",");
                if (temp.length != 2) {
                    continue;
                }
                Museum museum = new Museum();
                museum.setId(item.getInt("muse_ID"));
                museum.setIntroduce(item.getString("muse_Intro"));
                museum.setName(item.getString("muse_Name"));
                museum.setPos(item.getString("muse_Address"));
                museum.setImageSrc(item.getString("muse_Img"));
                museum.setLatLng(new LatLng(Double.valueOf(temp[1]), Double.valueOf(temp[0])));
                allMuseums.add(museum);
            }
        }
        catch (JSONException e) {
            Toast.makeText(ctx, "????????????", Toast.LENGTH_SHORT).show();
        }
        return allMuseums;
    }

    /**
     * ??????????????????????????????????????????????????????
     * @param mapRecentCard ????????????????????????
     */
    private void cardOnClick(MapRecentCard mapRecentCard) {
        if (mapRecentCard == null) return;
        Log.i("Card Click", mapRecentCard.getMuseumName());
        LatLng latLng = mapRecentCard.getLatLngPos();
        if (latLng == null) return;
        int id = mapRecentCard.getMuseumId();
        Museum museum = new Museum();
        museum.setLatLng(mapRecentCard.getLatLngPos());
        museum.setPos(mapRecentCard.getMuseumPos());
        museum.setName(mapRecentCard.getMuseumName());
        museum.setIntroduce(mapRecentCard.getMuseumInfo());
        museum.setImageSrc(mapRecentCard.getImageSrc());
        museum.setId(id);
        allMuseums.put(id, museum);
        currentMuseum = museum;
        addMark(id, latLng.longitude, latLng.latitude);
        gotoPosition(latLng, 18f);
    }

    /**
     * ???????????????
     */
    private void initMap() {
        mapView = (MapView) findViewById(R.id.bmapView);
        RoundView.setRadius(24, mapView);
        // ????????????????????????
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
        // ??????????????????
        uiSettings.setRotateGesturesEnabled(false);
    }

    /**
     * ????????????
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startLocation() {
        if (locationClient.isStarted()) {
            return;
        }
        if (!Permission.check(ctx, Manifest.permission.ACCESS_FINE_LOCATION)) {
            Permission.request(this, Manifest.permission.ACCESS_FINE_LOCATION);
            return;
        }
        locationClient.start();
    }

    /**
     * ??????????????????
     * @param requestCode requestCode
     * @param permissions ????????????
     * @param grantResults ??????
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
                        Toast.makeText(ctx, "????????????????????????????????????????????????!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * ?????????????????????????????????????????????
     */
    private void gotoLastLocation() {
        if (lastBDLocation == null) return;
        gotoPosition(lastBDLocation.getLongitude(), lastBDLocation.getLatitude(), 16);
    }

    /**
     * ??????????????????????????????????????????
     * @param longitude ??????
     * @param latitude ??????
     * @param zoom ????????????
     */
    private void gotoPosition(double longitude, double latitude, float zoom) {
        baiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
        // ???????????????????????????
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(zoom);
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    /**
     * ??????????????????????????????????????????
     * @param latLng ????????????
     * @param zoom ????????????
     */
    private void gotoPosition(LatLng latLng, float zoom) {
        gotoPosition(latLng.longitude, latLng.latitude, zoom);
    }

    /**
     * ??????????????????????????????
     * @param id ?????????ID??????????????????????????????ID
     * @param longitude ??????
     * @param latitude ??????
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
     * ???????????????????????????
     */
    private void removeAllMarkers() {
        for (MarkerWithEquals marker : allMarkers.keySet()) {
            marker.marker.remove();
        }
        allMarkers.clear();
    }

    /**
     * ?????????????????????????????????
     */
    private void initMapSearch() {
        mSearch = RoutePlanSearch.newInstance();
        OnGetRoutePlanResultListener listener = new OnGetRoutePlanResultListener() {
            private OverlayManager lastOverlay = null;
            /**
             * ??????????????????
             * @param walkingRouteResult ??????????????????
             */
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                List<WalkingRouteOverlay> overlays = new ArrayList<>();
                List<WalkingRouteLine> resultList = walkingRouteResult.getRouteLines();
                if (resultList == null) {
                    return;
                }
                for (int i = 0; i < resultList.size(); i ++) {
                    WalkingRouteOverlay overlay = new WalkingRouteOverlay(baiduMap);
                    overlay.setData(resultList.get(i));
                    if (lastOverlay != null) lastOverlay.removeFromMap();
                    overlay.addToMap();
                    lastOverlay = overlay;
                    overlays.add(overlay);
                    break;
                }
            }

            /**
             * ??????????????????
             * @param transitRouteResult ??????????????????
             */
            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
            }

            /**
             * ????????????????????????
             * @param massTransitRouteResult ????????????????????????
             */
            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

            }

            /**
             * ??????????????????
             * @param drivingRouteResult ??????????????????
             */
            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                List<DrivingRouteOverlay> overlays = new ArrayList<>();
                List<DrivingRouteLine> resultList = drivingRouteResult.getRouteLines();
                if (resultList == null) {
                    return;
                }
                for (int i = 0; i < resultList.size(); i ++) {
                    DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
                    overlay.setData(resultList.get(i));
                    if (lastOverlay != null) lastOverlay.removeFromMap();
                    overlay.addToMap();
                    lastOverlay = overlay;
                    overlays.add(overlay);
                    break;
                }
            }

            /**
             * ??????????????????
             * @param indoorRouteResult ??????????????????
             */
            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

            }

            /**
             * ??????????????????
             * @param bikingRouteResult ??????????????????
             */
            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        };
        mSearch.setOnGetRoutePlanResultListener(listener);
    }

    /**
     * ?????????????????????????????????????????????
     * @param start ?????????
     * @param end ?????????
     */
    private void getWalkingRouterLines(LatLng start, LatLng end) {
        if (mSearch == null) initMapSearch();
        PlanNode startNode = PlanNode.withLocation(start);
        PlanNode endNode = PlanNode.withLocation(end);
        // ????????????
        mSearch.walkingSearch((new WalkingRoutePlanOption())
                .from(startNode)
                .to(endNode));
    }

    /**
     * ?????????????????????????????????????????????
     * @param start ?????????
     * @param end ?????????
     */
    private void getDrivingRouterLine(LatLng start, LatLng end) {
        if (mSearch == null) initMapSearch();
        PlanNode startNode = PlanNode.withLocation(start);
        PlanNode endNode = PlanNode.withLocation(end);
        // ????????????
        mSearch.drivingSearch((new DrivingRoutePlanOption())
                .from(startNode)
                .to(endNode));
    }

    @Override
    public int getItemId() {
        return R.id.navigation_map;
    }

    private BDLocation lastBDLocation;
    private boolean getLocationAndJump = false;

    /**
     * ??????????????????????????????
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
            Log.i("LocationType", String.valueOf(bdLocation.getLocType()));
            if (locationScanPan == 0) {
                locationClient.stop();
            }
        }

        @Override
        public void onLocDiagnosticMessage(int i, int i1, String s) {
            Log.e("LocationError", s);
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
        if (mSearch != null) mSearch.destroy();
        super.onDestroy();
    }

    /**
     * ????????????Marker?????????????????????????????????????????????????????????map???key
     * ???????????????????????????equals????????????hashCode
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
