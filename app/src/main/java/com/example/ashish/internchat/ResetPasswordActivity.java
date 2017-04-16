package com.example.ashish.internchat;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText inputEmail;
    private Button btn_link;
    private FirebaseAuth mAuth;
    private static final String LOG_TAG = ResetPasswordActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        inputEmail = (EditText) findViewById(R.id.editTextEmail);
        btn_link = (Button) findViewById(R.id.reset_password_btn);
        mAuth = FirebaseAuth.getInstance();

        btn_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(ResetPasswordActivity.this,"Enter Email Id",Toast.LENGTH_SHORT).show();
                }

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this,"link send . check email ",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(ResetPasswordActivity.this,"Authorisation failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }
}
