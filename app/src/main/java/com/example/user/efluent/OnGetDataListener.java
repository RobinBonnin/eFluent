package com.example.user.efluent;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by imanerai on 23/8/17.
 */


public interface OnGetDataListener {
    //make new interface for call back
    void onSuccess(DataSnapshot dataSnapshot);
    void onStart();
    void onFailure();
}

