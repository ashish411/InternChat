package com.example.ashish.internchat;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private ImageButton btn_link;
    private FirebaseAuth mAuth;
    private static final String LOG_TAG = ResetPasswordActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = (EditText) findViewById(R.id.editTextEmail);
        btn_link = (ImageButton) findViewById(R.id.reset_password_btn);
        mAuth = FirebaseAuth.getInstance();
        final LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.resetPasswordActivity);
        btn_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Snackbar snackbar = Snackbar.make(mLinearLayout, getString(R.string.email_empty), Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return;
                }else {
                        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ResetPasswordActivity.this, "link send . check email ", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(ResetPasswordActivity.this, "Authorisation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    });
                }
            }
        });

    }
}
