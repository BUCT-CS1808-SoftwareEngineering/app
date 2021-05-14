package cn.edu.buct.se.cs1808;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import cn.edu.buct.se.cs1808.utils.RoundView;

public class UserInfoActivity extends AppCompatActivity {
    private ImageView userImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        userImage = (ImageView) findViewById(R.id.userInfoImage);
        RoundView.setRadiusWithDp(18, userImage);

        ImageView backButt = (ImageView) findViewById(R.id.userInfoBackButton);
        backButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.this.finish();
            }
        });
    }
}
