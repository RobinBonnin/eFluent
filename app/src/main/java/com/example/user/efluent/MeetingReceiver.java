package com.example.user.efluent;

import android.app.Activity;

import java.util.ArrayList;

public interface MeetingReceiver {

    public void setMeetings(ArrayList<Meeting> meetingList);

    public Activity getActivity();
}
