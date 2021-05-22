package cn.edu.buct.se.cs1808;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.MapRecentCard;
import cn.edu.buct.se.cs1808.utils.DensityUtil;
import cn.edu.buct.se.cs1808.utils.Museum;
import cn.edu.buct.se.cs1808.utils.RoundView;
import cn.edu.buct.se.cs1808.utils.User;

public class MineConcernedMuseumActivity extends AppCompatActivity {
    private LinearLayout museumList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_concerned_museum);

        museumList = (LinearLayout) findViewById(R.id.myConcernedMuseumList);

        ImageView backButt = (ImageView) findViewById(R.id.concernedBackButt);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MineConcernedMuseumActivity.this.finish();
            }
        });

        JSONObject userInfo = User.getUserInfo(this);
        if (userInfo == null) {
            Toast.makeText(this, "未登录，请重新登陆", Toast.LENGTH_SHORT).show();
            return;
        }
        int userId = -1;
        try {
            userId = userInfo.getInt("user_ID");
        }
        catch (JSONException e) {
            Toast.makeText(this, "登录异常，请重新登录", Toast.LENGTH_SHORT).show();
            return;
        }
        loadMuseumList(userId, 1, 300);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 分页加载关注的博物馆列表
     * @param id 博物馆ID
     * @param page 页号
     * @param size 页大小
     */
    private void loadMuseumList(int id, int page, int size) {
        JSONObject params = new JSONObject();
        try {
            params.put("pageIndex", page);
            params.put("pageSize", size);
            params.put("user_ID", id);
        }
        catch (JSONException e) {
            Toast.makeText(this, "博物馆列表加载失败，请稍后重试!", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiTool.request(this, ApiPath.GET_CONCERNED_MUSEUMS, params, (JSONObject rep) -> {
            String code = null;
            try {
                code = rep.getString("code");
            }
            catch (JSONException e) {
                code = "未知错误";
            }

            if (!"success".equals(code)) {
                Toast.makeText(this, "数据加载失败:" + code, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject info = rep.getJSONObject("info");
                JSONArray data = info.getJSONArray("items");
                if (data.length() == 0) {
                    Toast.makeText(this, "无关注的博物馆", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < data.length(); i ++) {
                    JSONObject item = data.getJSONObject(i);
                    int museId = item.getInt("muse_ID");
                    addCard(museId);
                }
            }
            catch (JSONException e) {
                Toast.makeText(this, "无关注的博物馆", Toast.LENGTH_SHORT).show();
            }

        }, (JSONObject error) -> {
            try {
                Toast.makeText(this, error.getString("info"), Toast.LENGTH_SHORT).show();
            }
            catch (JSONException e) {
                Toast.makeText(this, "请求失败，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 添加博物馆卡片
     * @param id 博物馆ID
     * @param name 博物馆名称
     * @param pos 博物馆地理位置
     * @param info 博物馆信息
     * @param imageurl 博物馆图片链接
     * @param latLng 博物馆地理位置
     */
    private void addCard(int id, String name, String pos, String info, String imageurl, LatLng latLng) {
        MapRecentCard mapRecentCard = new MapRecentCard(this);
        mapRecentCard.setAttr(id, name, pos, info, imageurl, latLng);
        museumList.addView(mapRecentCard);
        RoundView.setRadius(24, mapRecentCard);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mapRecentCard.getLayoutParams();
        lp.setMargins(0, 0, 0, DensityUtil.dip2px(this, 24));
        mapRecentCard.setLayoutParams(lp);
        // 添加卡片点击事件
        mapRecentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MineConcernedMuseumActivity.this, MuseumActivity.class);
                intent.putExtra("muse_ID", id);
                startActivity(intent);
            }
        });
    }

    private void addCard(int museumId) {
        JSONObject params = new JSONObject();
        try {
            params.put("muse_ID", museumId);
            params.put("pageIndex", 1);
            params.put("pageSize", 300);
        }
        catch (JSONException e) {
            Toast.makeText(this, "博物馆详细信息加载失败", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiTool.request(this, ApiPath.GET_ALL_MUSEUM_INFO, params, (JSONObject rep) -> {
            String code = null;
            try {
                code = rep.getString("code");
            }
            catch (JSONException e) {
                code = "未知错误";
            }

            if (!"success".equals(code)) {
                Toast.makeText(this, "数据加载失败:" + code, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject info = rep.getJSONObject("info");
                JSONArray data = info.getJSONArray("items");
                if (data.length() == 0) {
                    Toast.makeText(this, "无关注的博物馆", Toast.LENGTH_SHORT).show();
                }
                for (int i = 0; i < data.length(); i ++) {
                    JSONObject item = data.getJSONObject(i);
                    int museId = item.getInt("muse_ID");
                    if (!item.has("muse_Name")) {
                        continue;
                    }
                    if (museId != museumId) continue;
                    String museInfo = item.getString("muse_Intro");
                    int maxLength = 128;
                    if (museInfo.length() > maxLength) {
                        museInfo = museInfo.substring(0, maxLength) + "……";
                    }
                    String museName = item.getString("muse_Name");
                    String musePos = item.getString("muse_Address");
                    String museImage = item.getString("muse_Img");
                    addCard(museId, museName, musePos, museInfo, museImage, null);
                    break;
                }
            }
            catch (JSONException e) {
                Toast.makeText(this, "无关注的博物馆", Toast.LENGTH_SHORT).show();
            }
        }, (JSONObject error) -> {
            try {
                Toast.makeText(this, error.getString("body"), Toast.LENGTH_SHORT).show();
            }
            catch (JSONException e) {
                Toast.makeText(this, "请求失败，请稍后重试", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
