package cn.edu.buct.se.cs1808;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.utils.User;
import cn.edu.buct.se.cs1808.utils.Validation;

public class ChangeUserInfoActivity extends AppCompatActivity {
    private EditText newMailInput;
    private EditText newPhoneInput;
    private EditText newPasswordInput;
    private boolean fromLogin = false;

    private boolean isShowPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Secs1808);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user_info);

        newMailInput = (EditText) findViewById(R.id.newEmailInput);
        newPhoneInput = (EditText) findViewById(R.id.newPhoneInput);
        newPasswordInput = (EditText) findViewById(R.id.newPassInput);

        ImageView backButton = (ImageView) findViewById(R.id.changeInfoBackButt);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView showPass = (ImageView) findViewById(R.id.change_showPass);
        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShowPass = !isShowPass;
                if (isShowPass) {
                    newPasswordInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPass.setImageResource(R.drawable.wmxfff_eye);
                }
                else {
                    newPasswordInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    showPass.setImageResource(R.drawable.essay_no_eye);
                }
            }
        });

        Button changeButton = (Button) findViewById(R.id.confirmChangeButton);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = newMailInput.getText().toString();
                String phone = newPhoneInput.getText().toString();
                String password = newPasswordInput.getText().toString();
                String message;
                if ((message = Validation.lengthBetween(email, 6, 32, "??????")) != null) {
                    Toast.makeText(ChangeUserInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((message = Validation.lengthBetween(phone, 6, 16, "????????????")) != null) {
                    Toast.makeText(ChangeUserInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((message = Validation.lengthBetween(password, 6, 16, "??????")) != null) {
                    Toast.makeText(ChangeUserInfoActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                changeInfo(email, phone, password);
            }
        });

        initUserInfo();
    }

    /**
     * ?????????????????????
     */
    private void initUserInfo() {
        JSONObject userInfo = User.getUserInfo(this);
        if (userInfo == null) {
            // ????????????????????????????????????
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        try {
            String phone = userInfo.getString("user_Phone");
            String email = userInfo.getString("user_Email");
            String password = userInfo.getString("user_Passwd");
            newPasswordInput.setText(password);
            newPhoneInput.setText(phone);
            newMailInput.setText(email);
        }
        catch (JSONException e) {
            Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * ??????????????????
     * @param email ??????????????????
     * @param phone ????????????????????????
     * @param password ??????????????????
     */
    private void changeInfo(String email, String phone, String password) {
        JSONObject userInfo = User.getUserInfo(this);
        if (userInfo == null) {
            // ???????????????????????????????????????????????????
            Toast.makeText(this, "???????????????", Toast.LENGTH_SHORT).show();
            User.gotoLoginPage(this);
            return;
        }
        try {
            userInfo.put("user_Passwd", password);
            userInfo.put("user_Email", email);
            userInfo.put("user_Phone", phone);
            // ????????????????????????
            userInfo.remove("if_com");
            userInfo.remove("user_Avatar");
            userInfo.remove("token");
        }
        catch (JSONException e) {
            Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiTool.request(this, ApiPath.CHANGE_USER_INFO, userInfo, (JSONObject rep) -> {
            String code = null;
            try {
                code = rep.getString("code");
            }
            catch (JSONException e) {
                code = "????????????";
            }

            if (!"success".equals(code)) {
                Toast.makeText(this, "????????????" + code, Toast.LENGTH_SHORT).show();
                return;
            }
            // ?????????????????????????????????????????????token
            Toast.makeText(this, "?????????????????????????????????", Toast.LENGTH_SHORT).show();
            // ?????????????????????????????????????????????????????????????????????????????????true
            fromLogin = true;
            User.logout(this);
            User.gotoLoginPage(this);
        }, (JSONObject error) -> {
            try {
                Toast.makeText(this, error.getString("info"), Toast.LENGTH_SHORT).show();
            }
            catch (JSONException e) {
                Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (fromLogin) {
            finish();
        }
    }
}
