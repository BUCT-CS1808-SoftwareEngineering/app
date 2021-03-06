package cn.edu.buct.se.cs1808;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mingle.widget.LoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.utils.User;
import cn.edu.buct.se.cs1808.utils.Validation;

public class LoginPageActivity extends AppCompatActivity {
    private boolean showPass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Secs1808);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        ImageView showPassButt = (ImageView) findViewById(R.id.showPasswordButton);
        EditText usernameInput = (EditText) findViewById(R.id.usernameInput);
        EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        showPassButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPass = !showPass;
                if (showPass) {
                    passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showPassButt.setImageResource(R.drawable.wmxfff_eye);
                }
                else {
                    passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    showPassButt.setImageResource(R.drawable.essay_no_eye);
                }
            }
        });
        TextView gotoResisterPageButt = (TextView) findViewById(R.id.gotoRegisterPage);
        gotoResisterPageButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterPageActivity.class);
                startActivity(intent);
            }
        });

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String message = null;
                if ((message = Validation.lengthBetween(username, 1, 16, "?????????")) != null) {
                    Toast.makeText(LoginPageActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((message = Validation.lengthBetween(password, 1, 16, "??????")) != null) {
                    Toast.makeText(LoginPageActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                login(username, password);
            }
        });
    }

    /**
     * ????????????????????????
     * @param username ?????????
     * @param password ??????
     */
    private void login(String username, String password) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_Name", username);
            params.put("user_Passwd", password);
        }
        catch (JSONException e) {
            Toast.makeText(this, "????????????!", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiTool.request(this, ApiPath.CREATE_USER_AUTH_TOKEN, params, (JSONObject rep) -> {
            String code;
            try {
                code = rep.getString("code");
            }
            catch (JSONException e) {
                code = null;
            }
            // ???code=success?????????????????????
            if (!"success".equals(code)) {
                Toast.makeText(this, "??????????????????????????????????????????!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                String token = rep.getString("token");
                User.updateLoginStatus(this, token, new User.Event() {
                    @Override
                    public void onStatusUpdated(JSONObject userStatus) {
                        if (userStatus == null) {
                            Toast.makeText(LoginPageActivity.this, "??????????????????????????????????????????!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            String name = userStatus.getString("user_Name");
                            Toast.makeText(LoginPageActivity.this, "?????????: " + name, Toast.LENGTH_SHORT).show();
                        }
                        catch (JSONException ignore) {}
                        // ?????????????????????????????????
                        finish();
                    }
                });
            }
            catch (JSONException ignore) {}
        }, (JSONObject error) -> {
            try {
                Toast.makeText(this, error.getString("info"), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
