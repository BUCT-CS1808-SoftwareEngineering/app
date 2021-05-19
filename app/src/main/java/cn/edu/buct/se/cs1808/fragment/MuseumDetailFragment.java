package cn.edu.buct.se.cs1808.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.edu.buct.se.cs1808.MuseumActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.RoundImageView;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.utils.LoadImage;

public class MuseumDetailFragment extends Fragment{
    private int activityId;
    private Context ctx;
    private ArrayList<RoundImageView> objectImageArray= new ArrayList<>();
    private ArrayList<TextView> objectNameArray= new ArrayList<>();
    private RoundImageView exhibitionImage;
    private TextView exhibitionName;
    private TextView museumAdress;
    private TextView museumTime;
    private TextView museumIntro;
    private TextView museumPrice;
    private TextView objectMore;
    private TextView exhibitionMore;
    private LinearLayout objectContainer;
    private LinearLayout exhibitionContainer;
    private String museName,museIntro,museAddress,museOpentime,musePrice,museImg;
    private JSONArray objectJSONArray;

    private int museID;
    public MuseumDetailFragment(){
        activityId = R.layout.activity_museum_detail;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getContext();

        //
        //
        //对接博物馆接口获取信息
        //
        Bundle bundle=getArguments();
        if(bundle!=null){
            museID = bundle.getInt("muse_ID");
            museName = bundle.getString("muse_Name");
            museIntro = bundle.getString("muse_Intro");
            museAddress = bundle.getString("muse_Address");
            museOpentime = bundle.getString("muse_Opentime");
            musePrice = bundle.getString("muse_Price");
            museImg = bundle.getString("muse_Img");
        }


        return inflater.inflate(activityId,container,false);
    }

    @Override
    public void onViewCreated( @Nullable View view, Bundle savedInstanceState) {
        objectImageArray.add((RoundImageView) view.findViewById(R.id.museum_detail_objectimage1));
        objectImageArray.add((RoundImageView) view.findViewById(R.id.museum_detail_objectimage2));
        objectImageArray.add((RoundImageView) view.findViewById(R.id.museum_detail_objectimage3));
        objectImageArray.add((RoundImageView) view.findViewById(R.id.museum_detail_objectimage4));


        objectNameArray.add((TextView) view.findViewById(R.id.museum_detail_objectname1));
        objectNameArray.add((TextView) view.findViewById(R.id.museum_detail_objectname2));
        objectNameArray.add((TextView) view.findViewById(R.id.museum_detail_objectname3));
        objectNameArray.add((TextView) view.findViewById(R.id.museum_detail_objectname4));

        objectContainer = (LinearLayout) view.findViewById(R.id.museum_detail_objectcontainer);
        exhibitionContainer=(LinearLayout) view.findViewById(R.id.museum_detail_exhibitioncontainer);

        objectMore = (TextView) view.findViewById(R.id.museum_detail_objectmore);
        exhibitionMore = (TextView) view.findViewById(R.id.museum_detail_exhibitionmore);

        objectMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MuseumActivity)getActivity()).setCurrentFragment(1);
            }
        });
        exhibitionMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MuseumActivity)getActivity()).setCurrentFragment(2);
            }
        });
        museumAdress = (TextView) view.findViewById(R.id.museum_detail_adress);
        museumTime = (TextView) view.findViewById(R.id.museum_detail_time);
        museumIntro = (TextView) view.findViewById(R.id.museum_detail_intro);
        museumPrice = (TextView) view.findViewById(R.id.museum_detail_price);

        museumAdress.setText(museAddress);
        museumTime.setText(museOpentime);
        museumIntro.setText(museIntro);
        museumPrice.setText(musePrice);

        exhibitionImage = (RoundImageView) view.findViewById(R.id.museum_detail_exhibitionimage);
        exhibitionName = (TextView) view.findViewById(R.id.museum_detail_exhibitionname);

        objectJSONArray = new JSONArray();

        addObject();
        addExhibition();



        ArrayList<TextView> textArray = new ArrayList<>();
        textArray.add((TextView) view.findViewById(R.id.museum_detail_graph));
        textArray.add((TextView) view.findViewById(R.id.museum_detail_objectmore));
        textArray.add((TextView) view.findViewById(R.id.museum_detail_exhibitionmore));
        for(int i=0;i<textArray.size();i++){
            TextView tv = textArray.get(i);
            tv.setTextColor(android.graphics.Color.parseColor("#ee7712"));
        }


    }
    private void addObject(){
        JSONObject params = new JSONObject();
        try {
            params.put("muse_ID",museID);
            params.put("pageSize", 4);
            params.put("pageIndex", 1);
        }
        catch (JSONException e) {

        }
        ApiTool.request(ctx, ApiPath. GET_COLLECTION, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");

                for(int i=0;i<items.length();i++){
                    JSONObject it = items.getJSONObject(i);
                    objectJSONArray.put(objectJSONArray.length(),it);
                }
                setObject();
            }
            catch(JSONException e){
                deleteObject();
            }
        }, (JSONObject error) -> {
            // 请求失败
            deleteObject();
        });
    }

    private void addExhibition(){
        JSONObject params = new JSONObject();
        try {
            params.put("muse_ID",museID);
            params.put("pageSize", 1);
            params.put("pageIndex", 1);
        }
        catch (JSONException e) {

        }
        ApiTool.request(ctx, ApiPath. GET_EXHIBITIONS, params, (JSONObject rep) -> {
            // 请求成功，rep为请求获得的数据对象
            try{
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");

                JSONObject it = items.getJSONObject(0);

                objectJSONArray.put(objectJSONArray.length(),it);

                setExhibition(it);
            }
            catch(JSONException e){
                deleteExhibition();
            }
        }, (JSONObject error) -> {
            // 请求失败
            deleteExhibition();
        });
    }

    private void deleteObject(){
        objectContainer.setVisibility(View.GONE);
    }
    private void deleteExhibition(){
        exhibitionContainer.setVisibility(View.GONE);
    }
    private void setObject(){
        int num = objectImageArray.size();
        try{
            for(int i=0;i<num;i++){
                JSONObject it = objectJSONArray.getJSONObject(i);
                RoundImageView oImage = objectImageArray.get(i);
                LoadImage loader = new LoadImage(oImage);
                loader.setBitmap(it.getString("col_Photo"));
                TextView oName = objectNameArray.get(i);
                oName.setText(it.getString("col_Name").replaceAll("\\s*", ""));
            }
        }
        catch(JSONException e){
            deleteObject();
        }

    }
    private void setExhibition(JSONObject it){
        try{
            LoadImage loader = new LoadImage(exhibitionImage);
            loader.setBitmap(it.getString("exhib_Pic"));
            exhibitionName.setText(it.getString("exhib_Name").replaceAll("\\s*", ""));
        }
        catch(JSONException e){
            deleteExhibition();
        }

    }
}
