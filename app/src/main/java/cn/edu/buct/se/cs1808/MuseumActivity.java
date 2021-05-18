package cn.edu.buct.se.cs1808;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

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

import cn.edu.buct.se.cs1808.components.AppraiseCard;
import cn.edu.buct.se.cs1808.fragment.AppraiseFragment;
import cn.edu.buct.se.cs1808.fragment.EducationFragment;
import cn.edu.buct.se.cs1808.fragment.ExhibitionFragment;
import cn.edu.buct.se.cs1808.fragment.MuseumDetailFragment;
import cn.edu.buct.se.cs1808.fragment.MuseumPagerAdapter;
import cn.edu.buct.se.cs1808.fragment.NavBaseFragment;
import cn.edu.buct.se.cs1808.fragment.ObjectFragment;
import cn.edu.buct.se.cs1808.fragment.SettingFragmentNav;
import cn.edu.buct.se.cs1808.utils.CompatViewPager;

public class MuseumActivity extends AppCompatActivity {
    private List<Fragment> fragmentList;
    private Map<Integer, Integer> id2index;
    private CompatViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Class<? extends Fragment>> fragmentClassList = new ArrayList<>();
    private ArrayList<String> pageNameList = new ArrayList<>();
    private MuseumPagerAdapter pagerAdapter;
    private TextView scoreText;
    private boolean collectFlag;//收藏标志
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum);
        fragmentList = new ArrayList<>();
        id2index = new HashMap<>();
        viewPager = (CompatViewPager) findViewById(R.id.activity_museum_viewpage);
        tabLayout = (TabLayout) findViewById(R.id.activity_museum_tablayout);
        fragmentClassList.add(MuseumDetailFragment.class);
        fragmentClassList.add(ObjectFragment.class);
        fragmentClassList.add(ExhibitionFragment.class);
        fragmentClassList.add(EducationFragment.class);
        fragmentClassList.add(AppraiseFragment.class);
        pageNameList.add("详情");
        pageNameList.add("藏品");
        pageNameList.add("展览");
        pageNameList.add("教育");
        pageNameList.add("评价");

        Intent intent = getIntent();
        int museumId = intent.getIntExtra("muse_ID",1);

        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.reMeasureCurrentPage(viewPager.getCurrentItem());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //viewPager.reMeasureCurrentPage(viewPager.getCurrentItem());
            }
        });

        addFragment();
        initViewPage();

        scoreText = (TextView) findViewById(R.id.activity_museum_score);
        scoreText.setTextColor(android.graphics.Color.parseColor("#ee7712"));

        collectFlag = false;//暂时假定未收藏。。。。。。。。具体要看数据库
        if(collectFlag){
            setCollectButtonBackground(true);
        }
        else{
            setCollectButtonBackground(false);
        }
        //收藏按钮事件绑定
        ImageView collectButton = (ImageView) findViewById(R.id.activity_museum_collect);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectFlag = !collectFlag;
                if(collectFlag){
                    setCollectButtonBackground(true);
                }
                else{
                    setCollectButtonBackground(false);
                }
            }
        });
        //返回按钮事件绑定
        ImageView backButton = (ImageView) findViewById(R.id.activity_museum_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });
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
    private void initViewPage(){
        tabLayout.setupWithViewPager(viewPager,false);
        pagerAdapter = new MuseumPagerAdapter(fragmentList,pageNameList,getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }
    //收藏按钮点击反应
    private void setCollectButtonBackground(boolean mode){ //true转换收藏状态，false转换未收藏状态
        ImageView collectButton = (ImageView) findViewById(R.id.activity_museum_collect);
        if(mode){
            collectButton.setBackgroundResource(R.drawable.bblk_collect_1);
        }
        else{
            collectButton.setBackgroundResource(R.drawable.bblk_collect_0);
        }
    }
    //返回键点击事件
    public void backPage(){
        //暂定跳转回首页
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
    }
}
