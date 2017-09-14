package com.example.user.efluent;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class PagerAdapterInfoPatient extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public LoginManager login;
    public Patient patient;
    public Orthophonist ortho;

    public PagerAdapterInfoPatient(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabFragmentInfoPatient1 tab1 = new TabFragmentInfoPatient1();
                //System.out.println("Patient Name 3:" + patient.first_name);
                tab1.setInfo(patient);
                return tab1;
            case 1:
                TabFragmentInfoPatient2 tab2 = new TabFragmentInfoPatient2();
                tab2.patient = patient;
                tab2.ortho = ortho;
                tab2.login = login;
                Log.d("TEST", "Juste before the getList");
                login.getListOfExercises(patient, tab2);
                return tab2;
            case 2:
                TabFragmentInfoPatient3 tab3 = new TabFragmentInfoPatient3();
                login.getListOfExercises(patient,tab3);
                return tab3;
            case 3:
                TabFragmentInfoPatient4 tab4 = new TabFragmentInfoPatient4();
                tab4.login = login;
                tab4.patient = patient;
                return tab4;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}