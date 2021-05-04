package cn.edu.buct.se.cs1808;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import cn.edu.buct.se.cs1808.components.AppraiseScore;

public class SubmitAppraiseActivity extends AppCompatActivity {
    private TextView submitButton;
    private EditText submitText;
    private ImageButton backButton;
    private AppraiseScore submitScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise_submit);

        submitButton = (TextView) findViewById(R.id.activity_appraise_submit_add);
        submitText = (EditText) findViewById(R.id.activity_appraise_submit_text);
        backButton = (ImageButton) findViewById(R.id.activity_appraise_submit_back);
        submitScore = (AppraiseScore) findViewById(R.id.activity_appraise_submit_score);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });
    }
    public void backPage(){
        //暂定跳转回博物馆详情
        Intent intent = new Intent(this, MuseumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        this.startActivity(intent);
    }
}
