package com.example.user.efluent;

/**
 * Created by User on 24/05/2016.
 */
import android.content.Context;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.mindorks.placeholderview.ExpandablePlaceHolderView;

import java.util.ArrayList;

public class TabFragmentPatient1 extends Fragment {

    public Patient patient;
    public LoginManager login;
    ArrayList<Message> messages;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_patient_1, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final LottieAnimationView animationView = (LottieAnimationView) getActivity().findViewById(R.id.animation_view);
        animationView.playAnimation();
        animationView.setSpeed(0.2f);


        /*String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };*/
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.rowlayout, R.id.label,values);
        setListAdapter(adapter);*/

    }

    public void countExercisesNotDone(ArrayList<Exercise> exerciseList) {
        //this.exerciseList = exerciseList;
        ArrayList<Exercise> exerciseListFiltered = new ArrayList<Exercise>();

        for (Exercise exercise : exerciseList) {
            if (!exercise.done){
                Log.d("TEST", "J'ajoute l'exercise " + exercise.word + " est il fait " + exercise.done + " " + exercise.id);
                exerciseListFiltered.add(exercise);
            }
        }
        TextView nbOfexercises = getActivity().findViewById(R.id.nb_of_ex_left);
        nbOfexercises.setText("Il reste " + exerciseListFiltered.size() + " exercises.");
    }

    public void setMessageList(ArrayList<Message> messages){
        Context mContext = getActivity().getApplicationContext();
        ExpandablePlaceHolderView mExpandableView = (ExpandablePlaceHolderView) getActivity().findViewById(R.id.expandableView);
        for(Message data : messages){
            mExpandableView.addView(new HeadingView(mContext, data.getTitle()));
            mExpandableView.addView(new InfoView(mContext, data));
        }

    }

}

