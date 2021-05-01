package cn.edu.buct.se.cs1808;

import android.os.Bundle;
import android.widget.TableLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.buct.se.cs1808.fragment.AppraiseFragment;
import cn.edu.buct.se.cs1808.fragment.ExhibitionFragment;
import cn.edu.buct.se.cs1808.fragment.MuseumDetailFragment;
import cn.edu.buct.se.cs1808.fragment.MuseumPagerAdapter;
import cn.edu.buct.se.cs1808.fragment.NavBaseFragment;
import cn.edu.buct.se.cs1808.fragment.ObjectFragment;

public class MuseumActivity extends AppCompatActivity {
    private List<Fragment> fragmentList;
    private Map<Integer, Integer> id2index;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Class<? extends Fragment>> fragmentClassList = new ArrayList<>();
    private ArrayList<String> pageNameList = new ArrayList<>();
    private MuseumPagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum);
        fragmentList = new ArrayList<>();
        id2index = new HashMap<>();
        viewPager = (ViewPager) findViewById(R.id.activity_museum_viewpage);
        tabLayout = (TabLayout) findViewById(R.id.activity_museum_tablayout);
        fragmentClassList.add(MuseumDetailFragment.class);
        fragmentClassList.add(ObjectFragment.class);
        fragmentClassList.add(ExhibitionFragment.class);
        fragmentClassList.add(AppraiseFragment.class);
        pageNameList.add("详情");
        pageNameList.add("藏品");
        pageNameList.add("展览");
        pageNameList.add("评价");
        addFragment();
        initViewPage2();
    }
    private void addFragment(){
        int num = fragmentClassList.size();
        for(int i=0;i<num;i++){
            Fragment fragment;
            try {
                Class<? extends Fragment> fragmentClass = fragmentClassList.get(i);
                fragment = fragmentClass.newInstance();
            }
            catch (IllegalAccessException | InstantiationException e) {
                return;
            }
            //id2index.put(fragment.getItemId(), fragmentList.size());
            fragmentList.add(fragment);
        }
    }
    private void initViewPage2(){
        tabLayout.setupWithViewPager(viewPager,false);
        pagerAdapter = new MuseumPagerAdapter(fragmentList,pageNameList,getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }
}
