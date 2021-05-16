package cn.edu.buct.se.cs1808;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baidu.mapapi.model.LatLng;

import cn.edu.buct.se.cs1808.components.MapRecentCard;
import cn.edu.buct.se.cs1808.utils.DensityUtil;
import cn.edu.buct.se.cs1808.utils.RoundView;

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        for (int i = 0; i < 3; i ++) {
            addCard(i, "北京博物馆", "北京市昌平区某镇某路某地方", "这是北京博物馆", "https://pic.baike.soso.com/ugc/baikepic2/26022/cut-20190829122815-1940041223_jpg_751_600_36257.jpg/300", null);
        }
    }

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
                startActivity(intent);
            }
        });
    }
}
