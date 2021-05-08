package cn.edu.buct.se.cs1808;


import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterPageActivity extends AppCompatActivity {
    private boolean showPass = false;
    private boolean showConfirmPass = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ImageView showPassButt = (ImageView) findViewById(R.id.showPassButt);
        EditText passwordInput = (EditText) findViewById(R.id.registerPasswordInput);
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
        EditText confirmPasswordInput = (EditText) findViewById(R.id.registerPasswordConfirmInput);
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
    }
}
