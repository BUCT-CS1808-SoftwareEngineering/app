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

import org.json.JSONObject;

import cn.edu.buct.se.cs1808.utils.Validation;

public class RegisterPageActivity extends AppCompatActivity {
    private boolean showPass = false;
    private boolean showConfirmPass = false;

    private EditText usernameInput;
    private EditText passwordInput;
    private EditText confirmPasswordInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
                }
                else {
                    passwordInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
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
                }
                else {
                    confirmPasswordInput.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
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
                if ((message = Validation.lengthBetween(username, 1, 16, "用户名")) != null) {
                    Toast.makeText(RegisterPageActivity.this, message, Toast.LENGTH_SHORT).show();
                    return;
                }
                if ((message = Validation.lengthBetween(password, 1, 16, "密码")) != null) {
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
    }

}
