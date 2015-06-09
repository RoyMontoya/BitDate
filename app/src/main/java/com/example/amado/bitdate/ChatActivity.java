package com.example.amado.bitdate;

import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class ChatActivity extends ActionBarActivity implements View.OnClickListener, MessageDataSource.MessagesCallback{

    public static final String TAG = "ChatActivity";

    private ArrayList<Message> mMessages;
    private MessagesAdapter mAdapter;
    private User mRecipient;
    private ListView mList;
    private Handler mHandler = new Handler();
    private Date mLastMessageDate= new Date();
    private String mConversationId;
    private MessageDataSource.MessagesListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mRecipient=(User)getIntent().getSerializableExtra(MatchesFragment.USER_EXTRA);

        mList = (ListView)findViewById(R.id.messages_list);
        mMessages = new ArrayList<>();
        mAdapter = new MessagesAdapter(mMessages);
        mList.setAdapter(mAdapter);

        setTitle(mRecipient.getFirstName());

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Button sendButton = (Button)findViewById(R.id.send_button);
        sendButton.setOnClickListener(this);

        String[] ids = {mRecipient.getId(), UserDataSource.getCurrentUser().getId()};
        Arrays.sort(ids);
        mConversationId = ids[0]+ids[1];
        mListener = MessageDataSource.addMessagesListener(mConversationId, this);



    }


    public void onClick(View v) {
        EditText newMessageView = (EditText)findViewById(R.id.new_message);
        String newMessage = newMessageView.getText().toString();
        newMessageView.setText("");
        Message msg = new Message() ;
        msg.setDate(new Date());
        msg.setText(newMessage);
        msg.setSender(UserDataSource.getCurrentUser().getId());
        MessageDataSource.saveMessages(msg, mConversationId);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageDataSource.stop(mListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMessageAdded(Message message) {
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
    }

    private class MessagesAdapter extends ArrayAdapter<Message> {

        public MessagesAdapter(ArrayList<Message> messages) {
            super(ChatActivity.this, R.layout.message, R.id.message, messages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);

            Message message = getItem(position);

            TextView messageView = (TextView)convertView.findViewById(R.id.message);
            messageView.setText(message.getText());
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)
                    messageView.getLayoutParams();


            int sdk = Build.VERSION.SDK_INT;
            if(message.getSender().equals(UserDataSource.getCurrentUser().getId())){
                if(sdk>Build.VERSION_CODES.JELLY_BEAN) {
                    messageView.setBackground(getResources().getDrawable(R.drawable.bubble_right_green));
                }else{
                    messageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bubble_right_green));
                }

                layoutParams.gravity= Gravity.RIGHT;


            }else{
                if(sdk>Build.VERSION_CODES.JELLY_BEAN) {
                    messageView.setBackground(getResources().getDrawable(R.drawable.bubble_left_gray));
                }else{
                    messageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.bubble_left_gray));
                }
                layoutParams.gravity= Gravity.LEFT;
            }

            messageView.setLayoutParams(layoutParams);

            return convertView;
        }
    }
}
