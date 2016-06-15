package com.example.user.efluent;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class TabFragmentPro2 extends ListFragment implements MeetingReceiver{

    private ArrayList<Meeting> meetingList;
    private ArrayList<Patient> patient_list;
    public LoginManager login;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_2, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };*/
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.rowlayout, R.id.label,values);
        setListAdapter(adapter);*/

        final TabFragmentPro2 self = this;
        self.patient_list = login.patient_list;
        //System.out.println("TESTTTTTTT : " + self.patient_list.get(1).first_name);

        View addButton = getActivity().findViewById(R.id.GoAddRDV);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Test", "GoAddPatient");
                Intent intent = new Intent(getView().getContext(), GiveRendezvousActivity.class);
                GiveRendezvousActivity.login = login;
                startActivity(intent);
            }
        });
    }


    public void setMeetings(ArrayList<Meeting> meetingList){
        this.meetingList = meetingList;
        ArrayList<String> patient_names = new ArrayList<String>();

        for(Meeting meeting: meetingList ){
            System.out.println("first name: " + meeting.time.toString());
            patient_names.add(meeting.time.toString());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.rowlayout, R.id.label, patient_names.toArray(new String[patient_names.size()]));
        setListAdapter(adapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.i("test", "Click from the list");
                final String item = (String) parent.getItemAtPosition(position);
                Log.i("test", "Name of patient is: " + item);
            }
        });
    }

}