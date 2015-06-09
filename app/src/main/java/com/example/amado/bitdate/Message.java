package com.example.amado.bitdate;

import java.util.Date;

/**
 * Created by Amado on 08/06/2015.
 */
public class Message {
    private String mText;
    private String mSender;
    private Date mDate;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }
}
