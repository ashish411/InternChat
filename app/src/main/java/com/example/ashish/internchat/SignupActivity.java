package com.example.ashish.internchat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashish.internchat.Util.Utility;
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
    private DatabaseReference mFirebaseDatabaseRef;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mLinearLayout = (LinearLayout) findViewById(R.id.linLayout);
        auth = FirebaseAuth.getInstance();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(getString(R.string.register));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                View view = SignupActivity.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                Boolean isNetworkAv = isNetworkAvailable();

                if (!isNetworkAv) {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, getString(R.string.no_network), Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return;
                }

                Boolean isValidEmail = Utility.isEmailValid(inputEmail);
                if (!isValidEmail) {

                    Snackbar snackbar = Snackbar.make(mLinearLayout, getString(R.string.not_valid_email), Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    emailText.setText("");
                                }
                            });
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return;
                } else if (TextUtils.isEmpty(inputEmail)) {

                    Snackbar snackbar = Snackbar.make(mLinearLayout, getString(R.string.email_empty), Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return;
                } else if (TextUtils.isEmpty(inputPassword)) {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, getString(R.string.pwd_empty), Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return;
                } else if (inputPassword.length() < 5) {
                    Snackbar snackbar = Snackbar.make(mLinearLayout, getString(R.string.pwd_len_small), Snackbar.LENGTH_LONG)
                            .setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    passwordText.setText("");
                                }
                            });

                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);

                    snackbar.show();
                    return;
                } else {
                    auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Log.i(LOG_TAG, "Creation of user with email : onComplete" + task.isComplete());

                                    if (!task.isSuccessful()) {
                                        Snackbar snackbar = Snackbar.make(mLinearLayout, getString(R.string.authFail) + task.getException(), Snackbar.LENGTH_LONG);
                                        View sbView = snackbar.getView();
                                        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                        textView.setTextColor(Color.YELLOW);
                                        snackbar.show();
                                    } else {
                                        mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
                                        String userId = auth.getCurrentUser().getUid();

                                        Log.i(LOG_TAG, userId);

                                        UserDetail userDetail = new UserDetail(inputEmail, inputPassword, userId);
                                        mFirebaseDatabaseRef.child(userId).setValue(userDetail);

                                        startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                        // auth.signOut();
                                        Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                }
                            });
                }
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
