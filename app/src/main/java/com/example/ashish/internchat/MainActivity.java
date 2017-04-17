package com.example.ashish.internchat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ashish.internchat.Util.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private EditText emailText, passwordText;
    private Button btnSignUp, btnResetPassword;
    private FirebaseAuth auth;
    private ImageButton btnSignIn;
    private String email, pwd;
    public FrameLayout mFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_main);
        emailText = (EditText) findViewById(R.id.editTextEmail);
        passwordText = (EditText) findViewById(R.id.editTextPassword);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password_main);
        btnSignIn = (ImageButton) findViewById(R.id.img_sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        mFrameLayout = (FrameLayout) findViewById(R.id.layout);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignupActivity.class));
                finish();
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ResetPasswordActivity.class));
                finish();
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                email = emailText.getText().toString();
                pwd = passwordText.getText().toString();

                Utility utility = new Utility();
                Boolean isNetworkAv = isNetworkAvailable();

                if (!isNetworkAv) {
                    Snackbar snackbar = Snackbar.make(mFrameLayout, getString(R.string.no_network), Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return;
                }

                Boolean isValidEmail = Utility.isEmailValid(email);
                if (!isValidEmail) {

                    Snackbar snackbar = Snackbar.make(mFrameLayout, getString(R.string.not_valid_email), Snackbar.LENGTH_LONG)
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
                } else if (TextUtils.isEmpty(email)) {

                    Snackbar snackbar = Snackbar.make(mFrameLayout, getString(R.string.email_empty), Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return;
                } else if (TextUtils.isEmpty(pwd)) {
                    Snackbar snackbar = Snackbar.make(mFrameLayout, getString(R.string.pwd_empty), Snackbar.LENGTH_LONG);
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.YELLOW);
                    snackbar.show();
                    return;
                } else if (pwd.length() < 5) {
                    Snackbar snackbar = Snackbar.make(mFrameLayout, getString(R.string.pwd_len_small), Snackbar.LENGTH_LONG)
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
                    final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                    pd.setMessage("Loading");
                    pd.show();
                    auth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.i(LOG_TAG, "SignInComplete" + task.isSuccessful());
                            if (!task.isSuccessful()) {

                                Snackbar snackbar = Snackbar.make(mFrameLayout, getString(R.string.login_fail), Snackbar.LENGTH_LONG)
                                        .setAction("UNDO", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                emailText.setText("");
                                                passwordText.setText("");
                                            }
                                        });

                                View sbView = snackbar.getView();
                                TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                                textView.setTextColor(Color.YELLOW);
                                pd.dismiss();
                                snackbar.show();
                                Log.e(LOG_TAG, String.valueOf(task.getException()));

                            } else {
                                Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                                startActivity(new Intent(MainActivity.this, UsersActivity.class));
                                overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                            }
                        }
                    });
                }
            }


        });
    }


    public  Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
