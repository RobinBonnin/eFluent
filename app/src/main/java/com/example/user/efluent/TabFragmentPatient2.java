package com.example.user.efluent;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.util.Log;

import java.util.ArrayList;

public class TabFragmentPatient2 extends ListFragment implements ExerciseReceiver{

    LoginManager login;
    public static Patient patient;

    ArrayList<Exercise> exerciseList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_patient_2, container, false);
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

        /*View goExo = getActivity().findViewById(R.id.GoExoVocal);

        goExo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Test", "GoExoVocal");
                Intent intent = new Intent(getView().getContext(), ExerciseVocal.class);
                startActivity(intent);
            }
        });*/

    }
    public void setExercises(ArrayList<Exercise> exerciseList){
        //this.exerciseList = exerciseList;
        ArrayList<Exercise> exerciseListFiltered = new ArrayList<Exercise>();

        for(Exercise exercise: exerciseList ){
            if (!exercise.done){
                exerciseListFiltered.add(exercise);
            }
        }

        CustomExerciseListAdapter adapter  = new CustomExerciseListAdapter(getActivity(),
                exerciseListFiltered.toArray(new Exercise[exerciseListFiltered.size()]));
        setListAdapter(adapter);

        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.rowlayout, R.id.label, patient_names.toArray(new String[patient_names.size()]));
        setListAdapter(adapter);*/

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.i("test", "Click from the list");
                final Exercise item = (Exercise) parent.getItemAtPosition(position);
                Log.i("test", "Word of exercises is: in this list " + item.word);
                Intent intent;
                if (item.word.equals("Sonometre")){
                    intent = new Intent(getView().getContext(), Sonometre.class);
                    Sonometre.exercise = item;
                    Sonometre.login = login;
                    Sonometre.patient = patient;
                }
                else {
                    intent = new Intent(getView().getContext(), ExerciseVocal.class);
                    ExerciseVocal.exercise = item;
                    ExerciseVocal.login = login;
                    ExerciseVocal.patient = patient;
                    ExerciseVocal.previousActivity = getActivity();
                }

                startActivity(intent);
            }
        });
    }
}