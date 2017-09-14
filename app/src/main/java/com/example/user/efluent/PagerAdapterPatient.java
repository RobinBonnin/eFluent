package com.example.user.efluent;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class PagerAdapterPatient extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public LoginManager login;
    public Patient patient;


    public PagerAdapterPatient(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("TEST", "ALLLLO" + position);
        switch (position) {
            case 0:
                TabFragmentPatient1 tab1 = new TabFragmentPatient1();
                Log.i("TEST", " I AM IN THE GOOD ONE");
                tab1.patient = patient;
                tab1.login = login;
                login.getListOfExercises(patient, tab1);
                login.getListOfMessages(patient,tab1);
                return tab1;
            case 1:
                TabFragmentPatient2 tab2 = new TabFragmentPatient2();
                tab2.patient = patient;
                tab2.login = login;
                Log.i("TEST", "juste avant le getList");
                login.getListOfExercises(patient, tab2);
                return tab2;
            case 2:
                TabFragmentPatient3 tab3 = new TabFragmentPatient3();
                Log.d("ETS", "Je suis dans le meetings");
                login.getListOfMeetings(patient,tab3);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}