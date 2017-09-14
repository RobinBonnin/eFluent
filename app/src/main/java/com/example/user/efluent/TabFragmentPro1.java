package com.example.user.efluent;

import android.animation.ValueAnimator;
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
import android.widget.Button;
import android.widget.ProgressBar;


import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;

public class TabFragmentPro1 extends ListFragment {

    /*Arraylist of patients are retrieved from LoginManager*/
    private ArrayList<Patient> patient_list;
    public LoginManager login;
    LottieAnimationView animationView;
    public static Orthophonist ortho;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout. tab_fragment_1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        animationView= (LottieAnimationView) getView().findViewById(R.id.animation_view);
        animationView.playAnimation();



        final TabFragmentPro1 self = this;

        View addButton = getActivity().findViewById(R.id.GoAddPatient);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Test", "GoAddPatient");
                Intent intent = new Intent(getView().getContext(), AddPatientActivity.class);
                AddPatientActivity.fragmentICameFrom = self;
                AddPatientActivity.login = login;
                startActivity(intent);
            }
        });

        /*View addButtonInfo = getActivity().findViewById(R.id.GoPatientInfo);

        addButtonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Test", "GoInfoPatient");
                Intent intent = new Intent(getView().getContext(), InfoPatientActivity.class);
                startActivity(intent);
            }
        }); */

        /*final Button buttonPatientInfo = (Button) getView().findViewById(R.id.inscriptionMain);
        buttonPatientInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("test", "-> Info Patient");
                Intent intent = new Intent(v.getContext(), InfoPatientActivity.class);
                //ProActivity.login = login;
                startActivity(intent);


            }
        });*/

    }



    public void setPatients (final ArrayList<Patient> patient_list){
        this.patient_list = patient_list;
        System.out.println("DESDE EL Fragment");

        CustomPatientListAdapter adapter= new CustomPatientListAdapter(getActivity(),
                patient_list.toArray(new Patient[patient_list.size()])
        );
        animationView.setVisibility(View.INVISIBLE);


        setListAdapter(adapter);
        this.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.i("test", "Click from the list");

                Patient item = patient_list.get(position);

                Log.i("test","from the click: " + item.first_name);

                Log.i("Test", "GoInfoPatient");
                Intent intent = new Intent(getView().getContext(), InfoPatientActivity.class);
                InfoPatientActivity.patient = item;
                InfoPatientActivity.login = login;
                InfoPatientActivity.ortho = ortho;
                // pass fragment
                startActivity(intent);
            }
        });
    }

    public void AddPatientToList(Patient patient) {
        patient_list.add(patient);
        this.getListView().invalidateViews();
        System.out.println("Size of patient_list = "+patient_list.size());
        System.out.println("Size of patient_list = "+patient_list.size());
        System.out.println("Patient no 3 first name = " + patient_list.get(2).first_name);
    }
}
