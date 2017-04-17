package com.example.ashish.internchat;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChatActivity extends AppCompatActivity {

    private final String LOG_TAG = ChatActivity.class.getSimpleName();
    private FirebaseListAdapter<ChatMessage> adapter;
    private ListView mChatMessageList;
    private EditText mInputText;
    private Button mBtnSendMessage;
    private final String mFirebaseDbName = "messages";
    private FirebaseUser mUser;
    private DatabaseReference mFdb;
    private String USER_DB = "Users";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        displayChatMessage();
        Intent intent = getIntent();
        String chatPerson = intent.getStringExtra("sendData");
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mInputText = (EditText) findViewById(R.id.editTextChatInput);
        mBtnSendMessage = (Button) findViewById(R.id.btnSendMsg);
        getSupportActionBar().setTitle(chatPerson);
        Log.i(LOG_TAG,chatPerson);

        mBtnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessageToFirebase();
            }
        });
    }

    private void sendMessageToFirebase() {

        String inputText = mInputText.getText().toString();
        FirebaseDatabase.getInstance().getReference(mFirebaseDbName)
                .push().setValue(new ChatMessage(inputText, FirebaseAuth.getInstance()
                .getCurrentUser().getEmail()));
        mInputText.setText("");

    }

    private void displayChatMessage() {
        mChatMessageList = (ListView) findViewById(R.id.messageList);
        adapter = new FirebaseListAdapter<ChatMessage>(ChatActivity.this,ChatMessage.class,
                                                        R.layout.chat_message_layout,FirebaseDatabase.getInstance().getReference(mFirebaseDbName)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                Log.i(LOG_TAG,model.getMessageText() + model.getMessageEmail() + model.getMessageTime());
                messageUser.setText(model.getMessageEmail());
                messageTime.setText(DateFormat.format("dd-MM-yy (HH:mm)",model.getMessageTime()));
                messageText.setText(model.getMessageText());
            }
        };
        mChatMessageList.setAdapter(adapter);
    }
}
