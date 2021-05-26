package cn.edu.buct.se.cs1808.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.NewWebActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.BoxTest;
import cn.edu.buct.se.cs1808.components.NewsCard;
import cn.edu.buct.se.cs1808.components.RoundImageView;
import cn.edu.buct.se.cs1808.utils.JSONArraySort;

public class NewsFragmentNav extends NavBaseFragment {
    private View view;
    private LinearLayout newsTitleContainer;
    private Button button;
    private ScrollView scroll;
    private JSONArray newsArray;
    private int indexNum;
    private JSONArray museArray;
    private int museID;
    private EditText searchInput;
    private RoundImageView searchButton;
    public NewsFragmentNav() {
        activityId = R.layout.activity_news;
    }

    @Override
    public int getItemId() {
        return R.id.navigation_news;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;

        String museString="[{\"muse_Name\": \"故宫博物院\", \"muse_ID\": 1}, {\"muse_Name\": \"中国国家博物馆\", \"muse_ID\": 2}, {\"muse_Name\": \"上海科技馆\", \"muse_ID\": 3}, {\"muse_Name\": \"中国科学技术馆\", \"muse_ID\": 4}, {\"muse_Name\": \"浙江省博物馆\", \"muse_ID\": 5}, {\"muse_Name\": \"南京博物院\", \"muse_ID\": 6}, {\"muse_Name\": \"陕西历史博物馆\", \"muse_ID\": 7}, {\"muse_Name\": \"上海博物馆\", \"muse_ID\": 8}, {\"muse_Name\": \"河南博物院\", \"muse_ID\": 9}, {\"muse_Name\": \"重庆红岩革命历史博物馆\", \"muse_ID\": 10}, {\"muse_Name\": \"侵华日军南京大屠杀遇难同胞纪念馆\", \"muse_ID\": 11}, {\"muse_Name\": \"韶山毛泽东同志纪念馆\", \"muse_ID\": 12}, {\"muse_Name\": \"成都武侯祠博物馆\", \"muse_ID\": 13}, {\"muse_Name\": \"秦始皇帝陵博物院\", \"muse_ID\": 14}, {\"muse_Name\": \"南京市博物总馆\", \"muse_ID\": 15}, {\"muse_Name\": \"天津博物馆\", \"muse_ID\": 16}, {\"muse_Name\": \"重庆中国三峡博物馆\", \"muse_ID\": 17}, {\"muse_Name\": \"恭王府博物馆\", \"muse_ID\": 18}, {\"muse_Name\": \"山西博物院\", \"muse_ID\": 19}, {\"muse_Name\": \"甘肃省博物馆\", \"muse_ID\": 20}, {\"muse_Name\": \"宝鸡青铜器博物院\", \"muse_ID\": 21}, {\"muse_Name\": \"清华大学艺术博物馆\", \"muse_ID\": 22}, {\"muse_Name\": \"中国（海南）南海博物馆\", \"muse_ID\": 23}, {\"muse_Name\": \"山西地质博物馆\", \"muse_ID\": 24}, {\"muse_Name\": \"成都市博物馆\", \"muse_ID\": 25}, {\"muse_Name\": \"南京中国科举博物馆\", \"muse_ID\": 26}, {\"muse_Name\": \"中国港口博物馆\", \"muse_ID\": 27}, {\"muse_Name\": \"四渡赤水纪念馆\", \"muse_ID\": 28}, {\"muse_Name\": \"吐鲁番博物馆\", \"muse_ID\": 29}, {\"muse_Name\": \"维吾尔自治区博物馆\", \"muse_ID\": 30}, {\"muse_Name\": \"宁夏回族自治区固原博物馆\", \"muse_ID\": 31}, {\"muse_Name\": \"宁夏回族自治区博物馆\", \"muse_ID\": 32}, {\"muse_Name\": \"青海藏医药文化博物馆\", \"muse_ID\": 33}, {\"muse_Name\": \"青海省博物馆\", \"muse_ID\": 34}, {\"muse_Name\": \"平凉市博物馆\", \"muse_ID\": 35}, {\"muse_Name\": \"天水市博物馆\", \"muse_ID\": 36}, {\"muse_Name\": \"敦煌研究院\", \"muse_ID\": 37}, {\"muse_Name\": \"西安大唐西市博物馆\", \"muse_ID\": 38}, {\"muse_Name\": \"延安革命纪念馆\", \"muse_ID\": 39}, {\"muse_Name\": \"西安半坡博物馆\", \"muse_ID\": 40}, {\"muse_Name\": \"西安博物院\", \"muse_ID\": 41}, {\"muse_Name\": \"汉阳陵博物馆\", \"muse_ID\": 42}, {\"muse_Name\": \"西安碑林博物馆\", \"muse_ID\": 43}, {\"muse_Name\": \"西藏博物馆\", \"muse_ID\": 44}, {\"muse_Name\": \"云南民族博物馆\", \"muse_ID\": 45}, {\"muse_Name\": \"云南省博物馆\", \"muse_ID\": 46}, {\"muse_Name\": \"贵州省民族博物馆\", \"muse_ID\": 47}, {\"muse_Name\": \"贵州省博物馆\", \"muse_ID\": 48}, {\"muse_Name\": \"遵义会议纪念馆\", \"muse_ID\": 49}, {\"muse_Name\": \"5·12汶川特大地震纪念馆\", \"muse_ID\": 50}, {\"muse_Name\": \"四川省建川博物馆\", \"muse_ID\": 51}, {\"muse_Name\": \"朱德故居纪念馆\", \"muse_ID\": 52}, {\"muse_Name\": \"自贡市盐业历史博物馆\", \"muse_ID\": 53}, {\"muse_Name\": \"四川广汉三星堆博物馆\", \"muse_ID\": 54}, {\"muse_Name\": \"邓小平故居陈列馆\", \"muse_ID\": 55}, {\"muse_Name\": \"自贡恐龙博物馆\", \"muse_ID\": 56}, {\"muse_Name\": \"成都杜甫草堂博物馆\", \"muse_ID\": 57}, {\"muse_Name\": \"成都金沙遗址博物馆\", \"muse_ID\": 58}, {\"muse_Name\": \"四川博物院\", \"muse_ID\": 59}, {\"muse_Name\": \"大足石刻博物馆\", \"muse_ID\": 60}, {\"muse_Name\": \"重庆三峡移民纪念馆\", \"muse_ID\": 61}, {\"muse_Name\": \"重庆自然博物馆\", \"muse_ID\": 62}, {\"muse_Name\": \"海南省博物馆\", \"muse_ID\": 63}, {\"muse_Name\": \"桂林博物馆\", \"muse_ID\": 64}, {\"muse_Name\": \"广西民族博物馆\", \"muse_ID\": 65}, {\"muse_Name\": \"广西壮族自治区博物馆\", \"muse_ID\": 66}, {\"muse_Name\": \"广东中国客家博物馆\", \"muse_ID\": 67}, {\"muse_Name\": \"鸦片战争博物馆\", \"muse_ID\": 68}, {\"muse_Name\": \"广东海上丝绸之路博物馆\", \"muse_ID\": 69}, {\"muse_Name\": \"广州艺术博物院\", \"muse_ID\": 70}, {\"muse_Name\": \"广东民间工艺博物馆\", \"muse_ID\": 71}, {\"muse_Name\": \"广州博物馆\", \"muse_ID\": 72}, {\"muse_Name\": \"孙中山故居纪念馆\", \"muse_ID\": 73}, {\"muse_Name\": \"深圳博物馆\", \"muse_ID\": 74}, {\"muse_Name\": \"西汉南越王博物馆\", \"muse_ID\": 75}, {\"muse_Name\": \"广东省博物馆\", \"muse_ID\": 76}, {\"muse_Name\": \"胡耀邦同志纪念馆\", \"muse_ID\": 77}, {\"muse_Name\": \"长沙市博物馆\", \"muse_ID\": 78}, {\"muse_Name\": \"长沙简牍博物馆\", \"muse_ID\": 79}, {\"muse_Name\": \"刘少奇同志纪念馆\", \"muse_ID\": 80}, {\"muse_Name\": \"湖南省博物馆\", \"muse_ID\": 81}, {\"muse_Name\": \"长江文明馆\", \"muse_ID\": 82}, {\"muse_Name\": \"武汉市中山舰博物馆\", \"muse_ID\": 83}, {\"muse_Name\": \"辛亥革命武昌起义纪念馆\", \"muse_ID\": 84}, {\"muse_Name\": \"武汉革命博物馆\", \"muse_ID\": 85}, {\"muse_Name\": \"随州市博物馆\", \"muse_ID\": 86}, {\"muse_Name\": \"宜昌博物馆\", \"muse_ID\": 87}, {\"muse_Name\": \"荆州博物馆\", \"muse_ID\": 88}, {\"muse_Name\": \"武汉博物馆\", \"muse_ID\": 89}, {\"muse_Name\": \"湖北省博物馆\", \"muse_ID\": 90}, {\"muse_Name\": \"平顶山博物馆\", \"muse_ID\": 91}, {\"muse_Name\": \"中国文字博物馆\", \"muse_ID\": 92}, {\"muse_Name\": \"安阳博物馆\", \"muse_ID\": 93}, {\"muse_Name\": \"开封市博物馆\", \"muse_ID\": 94}, {\"muse_Name\": \"鄂豫皖苏区首府革命博物馆\", \"muse_ID\": 95}, {\"muse_Name\": \"郑州博物馆\", \"muse_ID\": 96}, {\"muse_Name\": \"南阳市汉画馆\", \"muse_ID\": 97}, {\"muse_Name\": \"洛阳博物馆\", \"muse_ID\": 98}, {\"muse_Name\": \"滕州市汉画像石馆\", \"muse_ID\": 99}, {\"muse_Name\": \"淄博市陶瓷博物馆\", \"muse_ID\": 100}, {\"muse_Name\": \"青岛山炮台遗址展览馆\", \"muse_ID\": 101}, {\"muse_Name\": \"山东大学博物馆\", \"muse_ID\": 102}, {\"muse_Name\": \"济南市章丘区博物馆\", \"muse_ID\": 103}, {\"muse_Name\": \"滕州市博物馆\", \"muse_ID\": 104}, {\"muse_Name\": \"曲阜市孔子博物院\", \"muse_ID\": 105}, {\"muse_Name\": \"济宁市博物馆\", \"muse_ID\": 106}, {\"muse_Name\": \"烟台市博物馆\", \"muse_ID\": 107}, {\"muse_Name\": \"齐国故城遗址博物馆\", \"muse_ID\": 108}, {\"muse_Name\": \"青岛啤酒博物馆\", \"muse_ID\": 109}, {\"muse_Name\": \"济南市博物馆\", \"muse_ID\": 110}, {\"muse_Name\": \"潍坊市博物馆\", \"muse_ID\": 111}, {\"muse_Name\": \"临沂市博物馆\", \"muse_ID\": 112}, {\"muse_Name\": \"中国甲午战争博物院\", \"muse_ID\": 113}, {\"muse_Name\": \"青州市博物馆\", \"muse_ID\": 114}, {\"muse_Name\": \"山东博物馆\", \"muse_ID\": 115}, {\"muse_Name\": \"赣州市博物馆\", \"muse_ID\": 116}, {\"muse_Name\": \"萍乡博物馆\", \"muse_ID\": 117}, {\"muse_Name\": \"江西省庐山博物馆\", \"muse_ID\": 118}, {\"muse_Name\": \"九江市博物馆\", \"muse_ID\": 119}, {\"muse_Name\": \"青岛市博物馆\", \"muse_ID\": 120}, {\"muse_Name\": \"景德镇中国陶瓷博物馆\", \"muse_ID\": 121}, {\"muse_Name\": \"八大山人纪念馆\", \"muse_ID\": 122}, {\"muse_Name\": \"安源路矿工人运动纪念馆\", \"muse_ID\": 123}, {\"muse_Name\": \"瑞金中央革命根据地纪念馆\", \"muse_ID\": 124}, {\"muse_Name\": \"井冈山革命博物馆\", \"muse_ID\": 125}, {\"muse_Name\": \"南昌八一起义纪念馆 \", \"muse_ID\": 126}, {\"muse_Name\": \"江西省博物馆\", \"muse_ID\": 127}, {\"muse_Name\": \"中央苏区历史博物馆\", \"muse_ID\": 128}, {\"muse_Name\": \"古田会议纪念馆\", \"muse_ID\": 129}, {\"muse_Name\": \"泉州海外交通史博物馆\", \"muse_ID\": 130}, {\"muse_Name\": \"福建中国闽台缘博物馆\", \"muse_ID\": 131}, {\"muse_Name\": \"福建博物院\", \"muse_ID\": 132}, {\"muse_Name\": \"蚌埠市博物馆\", \"muse_ID\": 133}, {\"muse_Name\": \"安徽省地质博物馆\", \"muse_ID\": 134}, {\"muse_Name\": \"宿州市博物馆\", \"muse_ID\": 135}, {\"muse_Name\": \"安徽中国徽州文化博物馆\", \"muse_ID\": 136}, {\"muse_Name\": \"淮北市博物馆\", \"muse_ID\": 137}, {\"muse_Name\": \"安徽博物院\", \"muse_ID\": 138}, {\"muse_Name\": \"杭州西湖博物馆\", \"muse_ID\": 139}, {\"muse_Name\": \"杭州工艺美术博物馆\", \"muse_ID\": 140}, {\"muse_Name\": \"舟山博物馆\", \"muse_ID\": 141}, {\"muse_Name\": \"嘉兴南湖革命纪念馆\", \"muse_ID\": 142}, {\"muse_Name\": \"中国茶叶博物馆\", \"muse_ID\": 143}, {\"muse_Name\": \"杭州博物馆\", \"muse_ID\": 144}, {\"muse_Name\": \"温州博物馆\", \"muse_ID\": 145}, {\"muse_Name\": \"宁波市天一阁博物馆\", \"muse_ID\": 146}, {\"muse_Name\": \"宁波博物馆\", \"muse_ID\": 147}, {\"muse_Name\": \"中国丝绸博物馆\", \"muse_ID\": 148}, {\"muse_Name\": \"浙江自然博物馆\", \"muse_ID\": 149}, {\"muse_Name\": \"雨花台烈士纪念馆\", \"muse_ID\": 150}, {\"muse_Name\": \"镇江博物馆\", \"muse_ID\": 151}, {\"muse_Name\": \"常州博物馆\", \"muse_ID\": 152}, {\"muse_Name\": \"常熟博物馆\", \"muse_ID\": 153}, {\"muse_Name\": \"无锡博物院\", \"muse_ID\": 154}, {\"muse_Name\": \"徐州博物馆\", \"muse_ID\": 155}, {\"muse_Name\": \"南通博物馆\", \"muse_ID\": 156}, {\"muse_Name\": \"苏州博物馆\", \"muse_ID\": 157}, {\"muse_Name\": \"扬州博物馆\", \"muse_ID\": 158}, {\"muse_Name\": \"龙华烈士纪念馆\", \"muse_ID\": 159}, {\"muse_Name\": \"上海中国航海博物馆\", \"muse_ID\": 160}, {\"muse_Name\": \"陈云纪念馆\", \"muse_ID\": 161}, {\"muse_Name\": \"上海鲁迅纪念馆\", \"muse_ID\": 162}, {\"muse_Name\": \"中国共产党第一次全国代表大会会址纪念馆\", \"muse_ID\": 163}, {\"muse_Name\": \"大庆市博物馆\", \"muse_ID\": 164}, {\"muse_Name\": \"黑龙江省民族博物馆\", \"muse_ID\": 165}, {\"muse_Name\": \"铁人王进喜纪念馆\", \"muse_ID\": 166}, {\"muse_Name\": \"瑷珲历史陈列馆\", \"muse_ID\": 167}, {\"muse_Name\": \"东北烈士纪念馆\", \"muse_ID\": 168}, {\"muse_Name\": \"黑龙江省博物馆\", \"muse_ID\": 169}, {\"muse_Name\": \"吉林省自然博物馆\", \"muse_ID\": 170}, {\"muse_Name\": \"吉林省博物院\", \"muse_ID\": 171}, {\"muse_Name\": \"大连自然博物馆\", \"muse_ID\": 172}, {\"muse_Name\": \"沈阳故宫博物院\", \"muse_ID\": 173}, {\"muse_Name\": \"大连博物馆\", \"muse_ID\": 174}, {\"muse_Name\": \"旅顺博物馆\", \"muse_ID\": 175}, {\"muse_Name\": \"“九·一八”历史博物馆\", \"muse_ID\": 176}, {\"muse_Name\": \"辽宁省博物馆\", \"muse_ID\": 177}, {\"muse_Name\": \"鄂尔多斯博物馆\", \"muse_ID\": 178}, {\"muse_Name\": \"赤峰市博物馆\", \"muse_ID\": 179}, {\"muse_Name\": \"内蒙古博物院\", \"muse_ID\": 180}, {\"muse_Name\": \"临汾市博物馆\", \"muse_ID\": 181}, {\"muse_Name\": \"大同市博物馆\", \"muse_ID\": 182}, {\"muse_Name\": \"中国煤炭博物馆\", \"muse_ID\": 183}, {\"muse_Name\": \"八路军太行纪念馆\", \"muse_ID\": 184}, {\"muse_Name\": \"邯郸市博物馆\", \"muse_ID\": 185}, {\"muse_Name\": \"西柏坡纪念馆\", \"muse_ID\": 186}, {\"muse_Name\": \"河北博物院\", \"muse_ID\": 187}, {\"muse_Name\": \"平津战役纪念馆\", \"muse_ID\": 188}, {\"muse_Name\": \"天津自然博物馆\", \"muse_ID\": 189}, {\"muse_Name\": \"周恩来邓颖超纪念馆\", \"muse_ID\": 190}, {\"muse_Name\": \"北京汽车博物馆\", \"muse_ID\": 191}, {\"muse_Name\": \"中国电影博物馆\", \"muse_ID\": 192}, {\"muse_Name\": \"中国印刷博物馆\", \"muse_ID\": 193}, {\"muse_Name\": \"北京天文馆\", \"muse_ID\": 194}, {\"muse_Name\": \"中国航空博物馆\", \"muse_ID\": 195}, {\"muse_Name\": \"周口店北京人遗址博物馆\", \"muse_ID\": 196}, {\"muse_Name\": \"首都博物馆\", \"muse_ID\": 197}, {\"muse_Name\": \"北京自然博物馆\", \"muse_ID\": 198}, {\"muse_Name\": \"中国人民抗日战争纪念馆\", \"muse_ID\": 199}, {\"muse_Name\": \"中国农业博物馆\", \"muse_ID\": 200}, {\"muse_Name\": \"中国地质博物馆\", \"muse_ID\": 201}, {\"muse_Name\": \"北京鲁迅博物馆\", \"muse_ID\": 202}, {\"muse_Name\": \"中国人民革命军事博物馆\", \"muse_ID\": 203}, {\"muse_Name\": \"长春市伪满皇宫博物院\", \"muse_ID\": 204}]";
        try{
            museArray = new JSONArray(museString);
        }
        catch(JSONException e){

        }
        museID = -1;
        indexNum = 1;
        button=(Button) view.findViewById(R.id.next);
        scroll=(ScrollView) view.findViewById(R.id.news_scroll);
        newsTitleContainer=(LinearLayout) view.findViewById(R.id.news_container);
        searchInput = (EditText) view.findViewById(R.id.news_input);
        searchButton = (RoundImageView) view.findViewById(R.id.news_button);
        newsArray = new JSONArray();
        addNewsBox(10,false);

//        myWebView = (WebView) findViewById(R.id.myWebView);
//
//        myWebView.loadUrl("http://www.baidu.com/");
//        myWebView.setWebViewClient(new WebViewClient());

        //initUI();

        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                searchClick();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                addNewsBox(10,true);
                button.setVisibility(View.INVISIBLE);
            }
        });
        scroll.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch(event.getAction()){
                    case MotionEvent.ACTION_MOVE:{
                        break;
                    }
                    case MotionEvent.ACTION_DOWN:{
                        break;
                    }
                    case MotionEvent.ACTION_UP:{
                        //当文本的measureheight 等于scroll滚动的长度+scroll的height
                        if(scroll.getChildAt(0).getMeasuredHeight()<=scroll.getScrollY()+scroll.getHeight()){
                            button.setVisibility(View.VISIBLE);
                        }else{

                        }
                        break;
                    }
                }
                return false;
            }
        });

    }
    private void addNewsBox(int num,boolean tip){
        JSONObject params = new JSONObject();
        try {
            if(museID!=-1){
                params.put("muse_ID", museID);
            }
            params.put("pageSize", num);
            params.put("pageIndex", indexNum);
        }
        catch (JSONException e) {

        }
        ApiTool.request(ctx, ApiPath. GET_NEWS_INFO, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                if(items.length()>0){
                    for(int i=0;i<items.length();i++){
                        JSONObject it = items.getJSONObject(i);
                        JSONObject newsParams = new JSONObject();
                        try{
                            newsParams.put("news_ID",it.getInt("news_ID"));
                            newsParams.put("news_Name",it.getString("news_Name"));
                            newsArray.put(newsArray.length(),newsParams);
                            generateNewsTitleBox(newsParams);
                            //newsParams.has()
                        }
                        catch (JSONException e){

                        }

                    }
                    indexNum+=1;
                }
                else{
                    if(tip){
                        Toast.makeText(ctx, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    }
                }

            }
            catch(JSONException e){
                if(tip){
                    Toast.makeText(ctx, "数据请求出错", Toast.LENGTH_SHORT).show();
                }
            }
        }, (JSONObject error) -> {
            // 请求失败
            if(tip){
                Toast.makeText(ctx, "数据请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void generateNewsTitleBox(JSONObject it)
    {
        String defauleName ="“五一”假期小长假：敦煌博物馆迎来游客参观高峰";
        String name;

        name=defauleName;


        try {
            name = it.getString("news_Name");
            NewsCard newsCard=new NewsCard(ctx);
            //设置属性
            newsCard.setTitle(name);
            RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 0, 0, dip2px(ctx,20));
            newsCard.setLayoutParams(lp);

            //获取自定义类内元素绑定事件
            TextView nName=newsCard.getNewsTitle();
            nName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openNewWebActivity(ctx,newsCard);
                }
            });
            newsTitleContainer.addView(newsCard);
        }
        catch (JSONException e){

        }
    }

    public void openNewWebActivity(Context context,NewsCard newsCard) {
        //页面跳转
        Intent intent = new Intent(context, NewWebActivity.class);
        TextView tv = newsCard.getNewsName();
        String name = tv.getText().toString();
        try{
            for(int i=0;i<newsArray.length();i++){
                JSONObject it = newsArray.getJSONObject(i);
                if(it.getString("news_Name")==name){
                    intent.putExtra("news_ID",it.getInt("news_ID"));
                    break;
                }
            }
        }
        catch(JSONException e){

        }
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    private void searchMuseumID(String name){
        String nameFilt=name.replaceAll("(博物(馆|院)?)|纪念(馆)?", "");
        if(nameFilt.length()!=0){
            name = nameFilt;
        }
        museArray = JSONArraySort.sortByKey(name,museArray,"muse_Name");
        try{
            JSONObject it = museArray.getJSONObject(0);
            museID = it.getInt("muse_ID");
        }
        catch(JSONException e){

        }
    }
    private void searchClick(){
        String name=searchInput.getText().toString();
        if(name.length()==0){
            return;
        }
        searchMuseumID(name);
        newsTitleContainer.removeAllViews();
        indexNum = 1;
        addNewsBox(10,true);
    }
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
