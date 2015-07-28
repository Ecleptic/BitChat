package com.example.cameron.bitchat;

import java.util.Date;

/**
 * Created by Cameron on 7/26/15.
 */
public class Message {

    private String mText;
    private String mSender;
    private Date mDate;

    Message(String text, String sender) {
        mText = text;
        mSender = sender;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
