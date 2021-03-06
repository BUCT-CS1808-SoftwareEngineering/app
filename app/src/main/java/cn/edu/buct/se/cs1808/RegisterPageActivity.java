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
                if ((message = Validation.equals(password, confirmPassword, "??????", "????????????")) != null) {
                    Toast.makeText(RegisterPageActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((message = Validation.lengthBetween(username, 2, 16, "?????????")) != null) {
                    Toast.makeText(RegisterPageActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((message = Validation.lengthBetween(password, 6, 16, "??????")) != null) {
                    Toast.makeText(RegisterPageActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                register(username, password);
            }
        });
    }

    /**
     * ??????????????????
     * @param username ????????????????????????
     * @param password ?????????????????????
     */
    private void register(String username, String password) {
        JSONObject params = new JSONObject();
        try {
            params.put("user_Name", username);
            params.put("user_Passwd", password);
            // ??????API??????????????????????????????????????????????????????
            params.put("user_Phone", "1234567890");
            params.put("user_Email", "user@mail.com");
        }
        catch (JSONException e) {
            Toast.makeText(this, "??????????????????????????????!", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiTool.request(this, ApiPath.ADD_USER, params, (JSONObject rep) -> {
            String code = null;
            try {
                code = rep.getString("code");
            }
            catch (JSONException e) {
                code = "????????????";
            }

            if (!"success".equals(code)) {
                Toast.makeText(this, "????????????: " + code, Toast.LENGTH_SHORT).show();
                return;
            }
            // ?????????????????????????????????????????????????????????????????????????????????finish??????
            Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show();
            finish();
        }, (JSONObject error) -> {
            try {
                Toast.makeText(this, "????????????: " + error.get("body"), Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                Toast.makeText(this, "????????????: ????????????", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
