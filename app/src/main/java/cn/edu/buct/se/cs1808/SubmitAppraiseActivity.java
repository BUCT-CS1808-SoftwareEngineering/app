package cn.edu.buct.se.cs1808;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.AppraiseScore;

public class SubmitAppraiseActivity extends AppCompatActivity {
    private TextView submitButton;
    private EditText submitText;
    private ImageButton backButton;
    private AppraiseScore submitScoreEnv;
    private AppraiseScore submitScoreExh;
    private AppraiseScore submitScoreSev;
    private int museumId;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Secs1808);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appraise_submit);

        Intent intent = getIntent();
        museumId = intent.getIntExtra("muse_ID",-1);
        userId = intent.getIntExtra("user_ID",-1);

        submitButton = (TextView) findViewById(R.id.activity_appraise_submit_add);
        submitText = (EditText) findViewById(R.id.activity_appraise_submit_text);
        backButton = (ImageButton) findViewById(R.id.activity_appraise_submit_back);
        submitScoreEnv = (AppraiseScore) findViewById(R.id.activity_appraise_submit_score_env);
        submitScoreExh = (AppraiseScore) findViewById(R.id.activity_appraise_submit_score_exh);
        submitScoreSev = (AppraiseScore) findViewById(R.id.activity_appraise_submit_score_sev);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backPage();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(museumId<0||userId<0){
                    Toast.makeText(v.getContext(), "????????????", Toast.LENGTH_SHORT).show();
                }
                else{
                    submit(true);
                }
            }
        });
    }
    public void submit(boolean tip){
        int score1 = submitScoreEnv.getScore();
        int score2 =submitScoreExh.getScore();
        int score3 = submitScoreSev.getScore();
        String text = submitText.getText().toString();
        if(text.length()>150){
            Toast.makeText(this, "?????????????????????150???", Toast.LENGTH_SHORT).show();
            return;
        }
        JSONObject params = new JSONObject();
        try {
            params.put("muse_ID", museumId);
            params.put("user_ID",userId);
            params.put("env_Review",score1);
            params.put("exhibt_Review",score2);
            params.put("service_Review",score3);
        }
        catch (JSONException e){

        }
        ApiTool.request(this, ApiPath. POST_USER_SCORE, params, (JSONObject rep) -> {
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
                    Log.e("111111",""+code);
                }
            }
            else{
                JSONObject paramsText = new JSONObject();
                try {
                    paramsText.put("muse_ID", museumId);
                    paramsText.put("user_ID",userId);
                    paramsText.put("com_Info",text);
                    paramsText.put("com_IfShow",true);
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String dateTime=df.format(new Date());
                    paramsText.put("com_Time",dateTime);

                }
                catch (JSONException e){

                }
                ApiTool.request(this, ApiPath. POST_COMMENT, paramsText, (JSONObject repText) -> {
                    // ???????????????rep??????????????????????????????
                    String codeText = null;
                    try {
                        codeText = repText.getString("code");
                    }
                    catch (JSONException e) {
                        codeText = "????????????";
                    }

                    if (!"success".equals(codeText)) {
                        if(tip){
                            Toast.makeText(this, "????????????: " + codeText, Toast.LENGTH_SHORT).show();
                        }
                        Log.e("222222",codeText);
                    }
                    else{
                        if(tip){
                            Toast.makeText(this, "????????????",Toast.LENGTH_SHORT).show();
                        }
                    }
                    backPage();

                }, (JSONObject error) -> {
                    if(tip){
                        try {
                            Toast.makeText(this, "????????????: " + error.get("body"), Toast.LENGTH_SHORT).show();
                            Log.e("333333",""+error.get("body"));
                        }
                        catch (JSONException e) {
                            Toast.makeText(this, "????????????: ????????????", Toast.LENGTH_SHORT).show();
                        }
                    }
                    backPage();
                });
            }
        }, (JSONObject error) -> {
            if(tip){
                try {
                    Toast.makeText(this, "????????????: " + error.get("body"), Toast.LENGTH_SHORT).show();
                    //Log.e("444444",""+error.get("body"));
                }
                catch (JSONException e) {
                    Toast.makeText(this, "????????????: ????????????", Toast.LENGTH_SHORT).show();
                }
            }
            backPage();
        });
    }
    public void backPage(){
        //??????????????????????????????
        Intent intent = new Intent(this, MuseumActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("muse_ID",museumId);
        this.startActivity(intent);
        this.finish();
    }
}
