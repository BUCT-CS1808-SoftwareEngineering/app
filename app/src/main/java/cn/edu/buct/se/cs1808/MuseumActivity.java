package cn.edu.buct.se.cs1808;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
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
import cn.edu.buct.se.cs1808.utils.JsonFileHandler;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.User;

public class MuseumActivity extends AppCompatActivity {
    private List<Fragment> fragmentList;
    private Map<Integer, Integer> id2index;
    private ScrollView scrollView;
    private CompatViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<Class<? extends Fragment>> fragmentClassList = new ArrayList<>();
    private ArrayList<String> pageNameList = new ArrayList<>();
    private MuseumPagerAdapter pagerAdapter;
    private TextView scoreText;
    private TextView nameText;
    private ImageView topImage;
    private JSONObject museumJSON;
    private boolean collectFlag;//????????????
    private boolean loginFlag;// ??????????????????
    private JSONObject userInfo;
    private int museumId;
    private int attId;
    private boolean attBlock;
    private int changeFlag;
    private boolean freshFlag=false;
    private int indexFragmentForm=0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        setTheme(R.style.Theme_Secs1808);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_museum);
        fragmentList = new ArrayList<>();
        id2index = new HashMap<>();
        scrollView = (ScrollView) findViewById(R.id.activity_museum_scroll);
        viewPager = (CompatViewPager) findViewById(R.id.activity_museum_viewpage);
        tabLayout = (TabLayout) findViewById(R.id.activity_museum_tablayout);
        nameText = (TextView) findViewById(R.id.activity_museum_name);
        scoreText = (TextView) findViewById(R.id.activity_museum_score);
        topImage = (ImageView) findViewById(R.id.activity_museum_topimage);
        fragmentClassList.add(MuseumDetailFragment.class);
        fragmentClassList.add(ObjectFragment.class);
        fragmentClassList.add(ExhibitionFragment.class);
        fragmentClassList.add(EducationFragment.class);
        fragmentClassList.add(AppraiseFragment.class);
        pageNameList.add("??????");
        pageNameList.add("??????");
        pageNameList.add("??????");
        pageNameList.add("??????");
        pageNameList.add("??????");

        Intent intent = getIntent();
        museumId = intent.getIntExtra("muse_ID",1);
        changeFlag = intent.getIntExtra("fragment_ID",-1);

        attBlock = false;

        viewPager.setOffscreenPageLimit(4);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.reMeasureCurrentPage(viewPager.getCurrentItem());
                indexFragmentForm=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //viewPager.reMeasureCurrentPage(viewPager.getCurrentItem());
            }
        });


        loginFlag =User.isLogin(this);
        if(loginFlag){
            userInfo = User.getUserInfo(this);
        }
        collectFlag = false;
        getCollectStatus(userInfo);

        //???????????????
        museumClick(false);

        getInfo(museumId);

        //????????????????????????
        ImageView collectButton = (ImageView) findViewById(R.id.activity_museum_collect);
        collectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(collectFlag){
                    setCollectStatus(true);
                }
                else{
                    setCollectStatus(true);
                }
            }
        });
        //????????????????????????
        ImageView backButton = (ImageView) findViewById(R.id.activity_museum_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });
        scoreText.setTextColor(android.graphics.Color.parseColor("#ee7712"));
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(freshFlag){
            addFragment();
            initViewPage();
            setCurrentFragment(indexFragmentForm);
        }
    }

    private void getInfo(int id){
        JSONObject params = new JSONObject();
        try {
            params.put("pageSize", 1);
            params.put("pageIndex", 1);
            params.put("muse_ID",id);
        }
        catch (JSONException e) {

        }
        ApiTool.request(this, ApiPath. GET_MUSEUM_INFO, params, (JSONObject rep) -> {
            // ???????????????rep??????????????????????????????
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                JSONObject it = items.getJSONObject(0);
                JSONObject scoreParams = new JSONObject();
                try{
                    double score[] = new double[1];
                    scoreParams.put("muse_ID",it.getInt("muse_ID"));
                    ApiTool.request(this, ApiPath.GET_MUSEUM_SCORE, scoreParams, (JSONObject repScore) -> {
                        // ???????????????rep??????????????????????????????
                        try{
                            JSONObject infoScore = repScore.getJSONObject("info");
                            double score1 = infoScore.getDouble("env_Review");
                            double score2 = infoScore.getDouble("exhibt_Review");
                            double score3 = infoScore.getDouble("service_Review");
                            score[0] = (score1+score2+score3)/3.0;
                            try{
                                it.put("muse_Score",score[0]);
                                showInfo(it);
                            }
                            catch (JSONException e){

                            }
                        }
                        catch(JSONException e){
                            score[0] = -1;
                            try{
                                it.put("muse_Score",score[0]);
                                showInfo(it);
                            }
                            catch (JSONException ee){

                            }
                        }
                    }, (JSONObject error) -> {
                        // ????????????
                        score[0] = -1;
                        try{
                            it.put("muse_Score",score[0]);
                            showInfo(it);

                        }
                        catch (JSONException e){

                        }
                    });
                }
                catch (JSONException e){

                }

            }
            catch(JSONException e){
            }

        }, (JSONObject error) -> {
            // ????????????

        });
    }
    private void showInfo(JSONObject it){
        museumJSON = it;
        try{
            double score = it.getDouble("muse_Score");
            if(score<0){
                scoreText.setText("--");
            }
            else{
                scoreText.setText(String.format("%.1f",it.getDouble("muse_Score")));
            }
            nameText.setText(it.getString("muse_Name"));

            String image = it.getString("muse_Img");
            if(image.length()==0){
                topImage.setImageResource(R.drawable.bleafumb_main_3);
            }
            LoadImage loader = new LoadImage(topImage);
            loader.setBitmap(image);

            addFragment();
            initViewPage();
            freshFlag = true;
            if(changeFlag>0){
                setCurrentFragment(changeFlag);
            }
        }
        catch (JSONException e){

        }

    }
    private void addFragment(){
        int num = fragmentClassList.size();
        fragmentList = new ArrayList<>();
        for(int i=0;i<num;i++){
            Fragment fragment;
            try {
                Class<? extends Fragment> fragmentClass = fragmentClassList.get(i);
                fragment = fragmentClass.newInstance();
                Bundle bundle = generateBundle();
                fragment.setArguments(bundle);
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
    private Bundle generateBundle(){
        int museID;
        String museName,museIntro,museAddress,museOpentime,musePrice,museImg;
        try{
            museID = museumJSON.getInt("muse_ID");

        }
        catch (JSONException e){
            museID = 1;
        }
        try{
            museName = museumJSON.getString("muse_Name");
        }
        catch (JSONException e){
            museName = "??????";
        }
        try{
            museIntro = museumJSON.getString("muse_Intro");
        }
        catch (JSONException e){
            museIntro = "??????";
        }
        try{
            museAddress = museumJSON.getString("muse_Address");
        }
        catch(JSONException e){
            museAddress = "??????";
        }
        try{
            museOpentime = museumJSON.getString("muse_Opentime");
        }
        catch(JSONException e){
            museOpentime = "??????";
        }
        try{
            musePrice = museumJSON.getString("muse_price");
        }
        catch(JSONException e){
            musePrice = "??????";
        }
        try{
            museImg = museumJSON.getString("muse_Img");
        }
        catch(JSONException e){
            museImg = "https://i01piccdn.sogoucdn.com/a80dfd4103e6994d";
        }

        Bundle bundle = new Bundle();
        bundle.putInt("muse_ID",museID);
        bundle.putString("muse_Name",museName);
        bundle.putString("muse_Intro",museIntro);
        bundle.putString("muse_Address",museAddress);
        bundle.putString("muse_Opentime",museOpentime);
        bundle.putString("muse_Price",musePrice);
        bundle.putString("muse_Img",museImg);
        return bundle;
    }
    //??????????????????
    private void getCollectStatus(JSONObject userInfo){
        if(!loginFlag){
            initCollectStatus();
            return;
        }
        try{
            int id = userInfo.getInt("user_ID");
            JSONObject params = new JSONObject();
            try {
                params.put("pageSize", 1);
                params.put("pageIndex", 1);
                params.put("user_ID",id);
                params.put("muse_ID",museumId);
            }
            catch (JSONException e) {
                userFail();
            }
            ApiTool.request(this, ApiPath. GET_CONCERNED_MUSEUMS, params, (JSONObject rep) -> {
                // ???????????????rep??????????????????????????????
                try{
                    JSONObject info = rep.getJSONObject("info");
                    JSONArray items = info.getJSONArray("items");
                    if(items.length()>0){
                        JSONObject it = items.getJSONObject(0);
                        collectFlag = true;
                        attId = it.getInt("att_ID");
                    }
                    attBlock = true;
                }
                catch(JSONException e){
                    userFail();
                }
                initCollectStatus();
            }, (JSONObject error) -> {
                // ????????????
                userFail();
                initCollectStatus();
            });

        }
        catch(JSONException e){

        }
    }
    private void initCollectStatus(){
        ImageView collectButton = (ImageView) findViewById(R.id.activity_museum_collect);
        if(collectFlag){
            collectButton.setBackgroundResource(R.drawable.bblk_collect_1);
        }
        else{
            collectButton.setBackgroundResource(R.drawable.bblk_collect_0);
        }
    }
    //????????????????????????
    private void setCollectStatus(boolean tip){ //true????????????????????????false?????????????????????
        if(!loginFlag){
            if(tip){
                Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        ImageView collectButton = (ImageView) findViewById(R.id.activity_museum_collect);
        if(attBlock){
            if(!collectFlag){
                try{
                    int id = userInfo.getInt("user_ID");
                    JSONObject params = new JSONObject();
                    try {
                        params.put("muse_ID", museumId);
                        params.put("user_ID",id);
                    }
                    catch (JSONException e){

                    }
                    ApiTool.request(this, ApiPath. POST_CONCERNED_MUSEUMS, params, (JSONObject rep) -> {
                        // ???????????????rep??????????????????????????????
                        String code = null;
                        try {
                            code = rep.getString("code");
                        }
                        catch (JSONException e) {
                            code = "????????????";
                        }

                        if (!"success".equals(code)) {
                            if(tip){
                                Toast.makeText(this, "????????????: " + code, Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            collectButton.setBackgroundResource(R.drawable.bblk_collect_1);
                            if(tip){
                                Toast.makeText(this, "???????????????????????????????????????", Toast.LENGTH_SHORT).show();
                            }
                            collectFlag = true;
                        }


                    }, (JSONObject error) -> {
                        if(tip){
                            try {
                                Toast.makeText(this, "????????????: " + error.get("body"), Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e) {
                                Toast.makeText(this, "????????????: ????????????", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                catch(JSONException e){
                    if(tip){
                        Toast.makeText(this, "????????????", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            else{
                JSONObject params = new JSONObject();
                String id = attId+"";
                try {
                    params.put("att_ID", id);
                }
                catch (JSONException e){

                }
                ApiTool.request(this, ApiPath. DELETE_CONCERNED_MUSEUMS, params, (JSONObject rep) -> {
                    // ???????????????rep??????????????????????????????
                    String code = null;
                    try {
                        code = rep.getString("code");
                    }
                    catch (JSONException e) {
                        code = "????????????";
                    }

                    if (!"success".equals(code)) {
                        if(tip){
                            Toast.makeText(this, "????????????: " + code, Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        collectButton.setBackgroundResource(R.drawable.bblk_collect_1);
                        if(tip){
                            Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                        collectButton.setBackgroundResource(R.drawable.bblk_collect_0);
                        collectFlag = false;
                    }


                }, (JSONObject error) -> {
                    if(tip){
                        try {
                            Toast.makeText(this, "????????????: " + error.get("body"), Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e) {
                            Toast.makeText(this, "????????????: ????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
    private void museumClick(boolean tip){
        JSONObject params = new JSONObject();
        try {
            params.put("muse_ID", museumId);
        }
        catch (JSONException e){

        }
        ApiTool.request(this, ApiPath. POST_MUSEUM_CLICK, params, (JSONObject rep) -> {
            // ???????????????rep??????????????????????????????
            String code = null;
            try {
                code = rep.getString("code");
            }
            catch (JSONException e) {
                code = "????????????";
            }

            if (!"success".equals(code)) {
                if(tip){
                    Toast.makeText(this, "????????????: " + code, Toast.LENGTH_SHORT).show();
                }
            }
            else{
                if(tip){
                    Toast.makeText(this, "??????", Toast.LENGTH_SHORT).show();
                }
            }
        }, (JSONObject error) -> {
            if(tip){
                try {
                    Toast.makeText(this, "????????????: " + error.get("body"), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e) {
                    Toast.makeText(this, "????????????: ????????????", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //?????????????????????
    public void backPage(){
        //?????????????????????
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
    }
    public void setCurrentFragment(int position){
        viewPager.setCurrentItem(position);
        scrollView.scrollTo(0,0);
    }

    private void userFail(){
        Toast.makeText(this, "???????????????????????????", Toast.LENGTH_SHORT).show();
    }
}
