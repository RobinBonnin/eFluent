package com.example.user.efluent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class TabFragmentInfoPatient4 extends Fragment {

    //private ArrayList<Patient> patient_list;
    LoginManager login;
    TabFragmentInfoPatient4 self = this;
    Patient patient;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_info_patient_4, container, false);
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

        /*View addButton = getActivity().findViewById(R.id.GoAddPatient);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Test", "GoAddPatient");
                Intent intent = new Intent(getView().getContext(), AddPatientActivity.class);
                startActivity(intent);
            }
        });*/
        final EditText message = getActivity().findViewById(R.id.editText);
        final EditText title = getActivity().findViewById(R.id.editText2);

        final Button addcomment = getActivity().findViewById(R.id.InfoPatientAddComment);
        addcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.sendMessage(message.getText().toString(), title.getText().toString(), patient, self);
            }
        });

    }

    public void sentSuccess (){
        final LottieAnimationView animationView = (LottieAnimationView) getActivity().findViewById(R.id.animation_view_sent);
        final EditText message = getActivity().findViewById(R.id.editText);
        final EditText title = getActivity().findViewById(R.id.editText2);
        animationView.setAnimation("success.json");
        Log.d("TEST", "I AM A ROBTO");
        animationView.setVisibility(View.VISIBLE);
        animationView.playAnimation();
        message.setText("");
        title.setText("");

    }


}
