package cn.edu.buct.se.cs1808;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.buct.se.cs1808.fragment.NavBaseFragment;
import cn.edu.buct.se.cs1808.fragment.IndexFragmentNav;
import cn.edu.buct.se.cs1808.fragment.MapFragmentNav;
import cn.edu.buct.se.cs1808.fragment.NewsFragmentNav;
import cn.edu.buct.se.cs1808.fragment.SettingFragmentNav;


public class MainActivity extends AppCompatActivity {
    private List<NavBaseFragment> fragmentList;
    private Map<Integer, Integer> id2index;
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Secs1808);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentList = new ArrayList<>();
        id2index = new HashMap<>();
        viewPager2 = ( ViewPager2 ) findViewById(R.id.mainViewPager);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);

        // 这里添加APP主页面
        // 注意添加顺序
        addFragment(IndexFragmentNav.class);
        addFragment(NewsFragmentNav.class);
        addFragment(MapFragmentNav.class);
        addFragment(SettingFragmentNav.class);

        initViewPager2();
        initBottomBar();


        // 初始化地图SDK设置
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    private void addFragment(Class<? extends NavBaseFragment> FragmentClass) {
        NavBaseFragment fragment = null;
        try {
            fragment = FragmentClass.newInstance();
        }
        catch (IllegalAccessException | InstantiationException e) {
            return;
        }
        id2index.put(fragment.getItemId(), fragmentList.size());
        fragmentList.add(fragment);
    }
    private void initViewPager2() {
        viewPager2.setAdapter(new FragmentStateAdapter(this) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                NavBaseFragment res = fragmentList.get(position);
                return (Fragment) res;
            }

            @Override
            public int getItemCount() {
                return fragmentList.size();
            }

        });
        // page change listener
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.setSelectedItemId(fragmentList.get(position).getItemId());
            }
        });
    }
    private void initBottomBar() {
        BottomNavigationView.OnNavigationItemSelectedListener listener;
        listener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int index = id2index.get(item.getItemId());
                viewPager2.setUserInputEnabled(false);
                viewPager2.setCurrentItem(index, false);
                return true;
            }
        };
        bottomNavigationView.setOnNavigationItemSelectedListener(listener);
    }

}
