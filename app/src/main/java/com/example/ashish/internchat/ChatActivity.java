package com.example.ashish.internchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ImageButton mBtnSendMessage, mBtnPicAttach;
    private final String mFirebaseDbName = "messages";
    private FirebaseUser mUser;
    private static final int REQUEST_IMAGE = 1;
    private View stickyViewSpacer;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        final Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String chatPerson = intent.getStringExtra("sendData");
        getSupportActionBar().setTitle(chatPerson);

        displayChatMessage();


        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mInputText = (EditText) findViewById(R.id.editTextChatInput);
        mBtnSendMessage = (ImageButton) findViewById(R.id.btnSendMsg);
        mBtnPicAttach = (ImageButton) findViewById(R.id.imageButtonPic);
        stickyViewSpacer = (View) findViewById(R.id.stickyViewPlaceholder);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listHeader = inflater.inflate(R.layout.list_header_layout, null);
        stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);

        Log.i(LOG_TAG, chatPerson);

        mChatMessageList.addHeaderView(listHeader);

        mChatMessageList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mChatMessageList.getFirstVisiblePosition() == 0) {
                    View firstChild = mChatMessageList.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();
                    }
                    mToolbar.setY(topY * 1f);
                }
            }
        });

        mBtnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessageToFirebase();
            }
        });

        mBtnPicAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    final Uri uri = data.getData();
                    Toast.makeText(ChatActivity.this, "pic selected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void sendMessageToFirebase() {


        String inputText = mInputText.getText().toString();
        if (inputText.isEmpty()) {
            LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.chatLayout);
            Snackbar snackbar = Snackbar.make(mLinearLayout, getString(R.string.msg_empty), Snackbar.LENGTH_LONG);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();
            return;
        }
        FirebaseDatabase.getInstance().getReference(mFirebaseDbName)
                .push().setValue(new ChatMessage(inputText, FirebaseAuth.getInstance()
                .getCurrentUser().getEmail()));
        mInputText.setText("");

    }

    private void displayChatMessage() {
        mChatMessageList = (ListView) findViewById(R.id.messageList);
        adapter = new FirebaseListAdapter<ChatMessage>(ChatActivity.this, ChatMessage.class,
                R.layout.chat_message_layout, FirebaseDatabase.getInstance().getReference(mFirebaseDbName)) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                TextView messageTime = (TextView) v.findViewById(R.id.message_time);
                TextView messageText = (TextView) v.findViewById(R.id.message_text);
                TextView messageDate = (TextView) v.findViewById(R.id.message_date);
                Log.i(LOG_TAG, model.getMessageText() + model.getMessageEmail() + model.getMessageTime());
                messageUser.setText(model.getMessageEmail());
                messageTime.setText(DateFormat.format("h:mm a", model.getMessageTime()));
                messageDate.setText(DateFormat.format("EEE, MMM d, ''yy\"", model.getMessageTime()));
                messageText.setText(model.getMessageText());
            }
        };
        mChatMessageList.setAdapter(adapter);

    }
}
