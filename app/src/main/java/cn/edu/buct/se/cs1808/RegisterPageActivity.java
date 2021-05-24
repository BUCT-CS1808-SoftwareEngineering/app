package cn.edu.buct.se.cs1808;


import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.utils.Validation;

public class RegisterPageActivity extends AppCompatActivity {
    private boolean showPass = false;
    private boolean showConfirmPass = false;

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Secs1808);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView showPassButt = (ImageView) findViewById(R.id.showPassButt);
        usernameInput = (EditText) findViewById(R.id.registUsernameInput);
        passwordInput = (EditText) findViewById(R.id.registerPasswordInput);
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

        ImageView showConfirmPassButt = (ImageView) findViewById(R.id.showConfirmPassButt);
        confirmPasswordInput = (EditText) findViewById(R.id.registerPasswordConfirmInput);
        showConfirmPassButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmPass = !showConfirmPass;
                if (showConfirmPass) {
                    confirmPasswordInput.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    showConfirmPassButt.setImageResource(R.drawable.wmxfff_eye);
                }
                else {
                    confirmPasswordInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    showConfirmPassButt.setImageResource(R.drawable.essay_no_eye);
                }
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameInput.getText().toString();
                String password = passwordInput.getText().toString();
                String confirmPassword = confirmPasswordInput.getText().toString();
                String message = null;
                if ((message = Validation.equals(password, confirmPassword, "密码", "确认密码")) != null) {
                    Toast.makeText(RegisterPageActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((message = Validation.lengthBetween(username, 2, 16, "用户名")) != null) {
                    Toast.makeText(RegisterPageActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((message = Validation.lengthBetween(password, 6, 16, "密码")) != null) {
                    Toast.makeText(RegisterPageActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                register(username, password);
            }
        });
    }

    /**
     * 执行注册操作
     * @param username 注册时候的用户名
     * @param password 注册时候的密码
     */
    private void register(String username, String password) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_Name", username);
            params.put("user_Passwd", password);
            // 后端API不知道怎么回事必须需要这两个参数……
            params.put("user_Phone", "1234567890");
            params.put("user_Email", "user@mail.com");
        }
        catch (JSONException e) {
            Toast.makeText(this, "注册失败，请稍后重试!", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiTool.request(this, ApiPath.ADD_USER, params, (JSONObject rep) -> {
            String code = null;
            try {
                code = rep.getString("code");
            }
            catch (JSONException e) {
                code = "未知错误";
            }

            if (!"success".equals(code)) {
                Toast.makeText(this, "注册失败: " + code, Toast.LENGTH_SHORT).show();
                return;
            }
            // 注册成功，跳转回页面，一般上级页面是登录页面，因此直接finish即可
            Toast.makeText(this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
            finish();
        }, (JSONObject error) -> {
            try {
                Toast.makeText(this, "请求失败: " + error.get("body"), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(this, "请求失败: 未知错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
