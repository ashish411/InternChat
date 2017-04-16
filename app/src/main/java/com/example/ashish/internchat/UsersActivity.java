package com.example.ashish.internchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private TextView noUserText;
    private ListView mUserList;
    private ProgressDialog pd;
    private DatabaseReference mFirebaseDatabaseRef;
    private static final String LOG_TAG = UsersActivity.class.getSimpleName();
    private String mUserDb = "Users";
    private List<String> mUserArray;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            mAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user == null){
                    startActivity(new Intent(UsersActivity.this,MainActivity.class));
                    finish();
                }

            }
        };

        mFirebaseDatabaseRef = FirebaseDatabase.getInstance().getReference(mUserDb);
        mUserArray = new ArrayList<>();
        noUserText = (TextView) findViewById(R.id.noUsersText);
        mUserList = (ListView) findViewById(R.id.usersList);

        mFirebaseDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    UserDetail user = ds.getValue(UserDetail.class);
                    String name = user.getmUserName();
                    mUserArray.add(name);
                }

                mUserList.setAdapter(new ArrayAdapter<String>(UsersActivity.this,android.R.layout.simple_list_item_1,mUserArray));

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mUserList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(UsersActivity.this,ChatActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.users_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.signOutAction :
                mAuth.signOut();
                return true;

            default:
               return super.onOptionsItemSelected(item);
        }

    }
}
