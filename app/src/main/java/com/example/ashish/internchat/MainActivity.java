package com.example.ashish.internchat;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText emailText,passwordText;
    private Button btnSignIn , btnSignUp,btnResetPassword;
    private FirebaseAuth auth;
    private String email,pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);

        emailText = (EditText) findViewById(R.id.editTextEmail);
        passwordText = (EditText) findViewById(R.id.editTextPassword);
        btnResetPassword = (Button)findViewById(R.id.btn_reset_password_main);
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SignupActivity.class));
                finish();
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ResetPasswordActivity.class));
                finish();
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                    email = emailText.getText().toString();
                    pwd = passwordText.getText().toString();
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this,"Enter Email Id",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pwd)){
                    Toast.makeText(MainActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.length()<5){
                    Toast.makeText(MainActivity.this,"Password should be atleast 5 character long",Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.i(LOG_TAG,"SignInComplete"+task.isSuccessful());
                        if (!task.isSuccessful()){
                            if (pwd.length()<5){
                                Toast.makeText(MainActivity.this,"Password should be atleast 5 character long",Toast.LENGTH_SHORT).show();
                                passwordText.setError("Password should be atleast 5 character long");
                                return;
                            }
                            Toast.makeText(MainActivity.this,"fail",Toast.LENGTH_SHORT).show();
                            Log.e(LOG_TAG, String.valueOf(task.getException()));

                        }
                        else {
                            Toast.makeText(MainActivity.this,"Login successful",Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(MainActivity.this,UsersActivity.class));
                        }
                    }
                });


            }
        });
    }
}
