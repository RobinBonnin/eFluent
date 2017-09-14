package com.example.user.efluent;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapterPro extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public LoginManager login;
    public Patient patient;
    public Orthophonist ortho;

    public PagerAdapterPro(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TabFragmentPro1 tab1 = new TabFragmentPro1();
                login.patientList(tab1);
                tab1.login = login;
                tab1.ortho = ortho;
                return tab1;
            case 1:
                TabFragmentPro2 tab2 = new TabFragmentPro2();
                tab2.login = login;
                tab2.ortho = ortho;
                login.getListOfMeetings(ortho,tab2);
                login.patientList(tab2);
                return tab2;
            case 2:
                TabFragmentPro3 tab3 = new TabFragmentPro3();
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