package com.example.cameron.bitchat;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cameron on 7/26/15.
 */
public class ContactDataSource implements LoaderManager.LoaderCallbacks<Cursor> {
    private static Contact sCurrentUser;

    private Context mContext;
    private Listener mListener;

    ContactDataSource(Context context, Listener listener){
        mContext = context;
        mListener = listener;
    }

    public static Contact getCurrentUser(){
        if (sCurrentUser == null && ParseUser.getCurrentUser() != null){
            sCurrentUser = new Contact();
            sCurrentUser.setPhoneNumber(ParseUser.getCurrentUser().getUsername());
            sCurrentUser.setName((String)ParseUser.getCurrentUser().get("name"));
        }
        return sCurrentUser;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                mContext,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone._ID,ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME},
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<String> numbers = new ArrayList<>();
        data.moveToFirst();
        while(!data.isAfterLast()){
            String phoneNumber = data.getString(1);
            phoneNumber = phoneNumber.replaceAll("[^0-9]","");

            numbers.add(phoneNumber);
            numbers.add("Ecleptic");
            data.moveToNext();
        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
//        query.whereContainedIn("username",numbers);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> parseUsers, ParseException e) {
                if (e == null){
                    ArrayList<Contact> contacts = new ArrayList<Contact>();
                    for (ParseUser parseUser: parseUsers){
                        Contact contact = new Contact();
                        contact.setName( parseUser.getString("name"));
                        contact.setPhoneNumber(parseUser.getUsername());
                        contacts.add(contact);
                    }
                    if (mListener != null){
                        mListener.onFetchedContacts(contacts);
                    }
                }else{

                }
            }
        });
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public interface Listener{
        void onFetchedContacts(ArrayList<Contact> contacts);
    }
}