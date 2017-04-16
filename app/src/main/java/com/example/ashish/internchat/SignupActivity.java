package com.example.ashish.internchat;

import android.content.Intent;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private static final String LOG_TAG = SignupActivity.class.getSimpleName();
    private EditText emailText, passwordText;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private FirebaseAuth auth;
    private final String firebaseUserJson = "https://internchat-89dee.firebaseio.com/Users";
    private DatabaseReference mFirebaseDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        auth = FirebaseAuth.getInstance();

        emailText = (EditText) findViewById(R.id.editTextEmail);
        passwordText = (EditText) findViewById(R.id.editTextPassword);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);
        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String inputEmail = emailText.getText().toString();
                final String inputPassword = passwordText.getText().toString();

                if (TextUtils.isEmpty(inputEmail)) {
                    Toast.makeText(SignupActivity.this, "Enter Email Id", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(inputPassword)) {
                    Toast.makeText(SignupActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (inputPassword.length() < 5) {
                    Toast.makeText(SignupActivity.this, "Password should be atleast 5 character long", Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.i(LOG_TAG, "Creation of user with email : onComplete" + task.isComplete());

                                if (!task.isComplete()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed ." + task.getException(), Toast.LENGTH_SHORT).show();
                                } else {

                                    mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
                                    String userId = mFirebaseDatabaseRef.push().getKey();

                                    UserDetail userDetail = new UserDetail(inputEmail,inputPassword);
                                    mFirebaseDatabaseRef.child(userId).setValue(userDetail);

                                    startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                   // auth.signOut();
                                    Toast.makeText(SignupActivity.this,"Account Created",Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            }
                        });
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });
    }
}
