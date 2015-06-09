package com.example.amado.bitdate;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Amado on 08/06/2015.
 */
public class MessageDataSource {
    private static final Firebase sRef = new Firebase("https://bitdate-mx.firebaseio.com/messages");
    private static SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddmmss");
    private static final String TAG ="MessageDataSource";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_SENDER = "sender";

    public static void saveMessages(Message message, String conversationId){
        Date date = message.getDate();

        String key = sDateFormat.format(date);
        HashMap<String, String> msg = new HashMap<>();
        msg.put(COLUMN_TEXT, message.getText());
        msg.put(COLUMN_SENDER, message.getSender());
        sRef.child(conversationId).child(key).setValue(msg);

    }

    public static MessagesListener addMessagesListener(String conversationId, final MessagesCallback callbacks ){
        MessagesListener listener = new MessagesListener(callbacks);
        sRef.child(conversationId).addChildEventListener(listener);
        return listener;
}

    public static void stop(MessagesListener listener){
        sRef.removeEventListener(listener);
    }



    public static class MessagesListener implements ChildEventListener{
        private MessagesCallback callbacks;
        MessagesListener(MessagesCallback callbacks) {
            this.callbacks = callbacks;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap<String, String> msg = (HashMap)dataSnapshot.getValue();
            Message message = new Message();
            message.setSender(msg.get(COLUMN_SENDER));
            message.setText(msg.get(COLUMN_TEXT));
            try {
                message.setDate(sDateFormat.parse(dataSnapshot.getKey()));
            }catch (Exception e){
                Log.d(TAG, "couldn't parse data "+e);
            }
            if(callbacks!=null){
                callbacks.onMessageAdded(message);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    }

    public interface MessagesCallback{
        public void onMessageAdded(Message message);
    }

}







