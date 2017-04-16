package com.example.ashish.internchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChatActivity extends AppCompatActivity {

    private ListView mChatMessageList;
    private EditText mInputText;
    private Button mBtnSendMessage;
    private final String mFirebaseDbName = "messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mChatMessageList = (ListView) findViewById(R.id.messageList);
        mInputText = (EditText) findViewById(R.id.editTextChatInput);
        mBtnSendMessage = (Button) findViewById(R.id.btnSendMsg);

        mBtnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = mInputText.getText().toString();
                FirebaseDatabase.getInstance().getReference(mFirebaseDbName)
                        .push().setValue(new ChatMessage(inputText, FirebaseAuth.getInstance()
                .getCurrentUser().getDisplayName()));
            }
        });

        mInputText.setText("");
    }
}
