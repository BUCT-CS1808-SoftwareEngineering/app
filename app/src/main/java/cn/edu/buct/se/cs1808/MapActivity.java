package cn.edu.buct.se.cs1808;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.MapView;


public class MapActivity extends AppCompatActivity {
    private MapView mapView = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView = (MapView) findViewById(R.id.bmapView);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
