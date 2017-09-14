package com.example.user.efluent;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.CircularProgressButton;

import java.util.ArrayList;

public class GiveExerciseActivity extends AppCompatActivity {

    String[] themeExo = new String[]{"Choose the theme :","Reeducation Orale", "Reeducation Ecrite", "Rythme"};
    String[] typeExo = new String[]{"Choose the type : ","Reconnaissance Vocale", "Puissance Voix"};
    String[] listExercisesVocal = new String[]{"Choose the exercise :",
            "baba",
            "dada",
            "deux",
            "jeux",
            "noeud",
            "papa",
            "tata",
            "voeux"};
    String[] listExercisesPuissance = new String[]{"Choose the exercise :","Sonometre"};
    String[] emptyList = new String[]{};

    //Strings to be sent to the server
    String theme = null;
    String type = null;
    String nameExo = null;
    ListView list;
    TextView choose;
    public static Activity previousActivity;




    public static Patient patient_to_add;
    public static LoginManager login;
    Animation translateOut;
    Animation translateIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_exercise);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Give an exercise to : " + patient_to_add.first_name + " " +
                patient_to_add.last_name);
        final Button reset = (Button) findViewById(R.id.buttonreset);


        final Button buttonGiveExo = (Button) findViewById(R.id.buttonGiveExercise);
        choose = (TextView) findViewById(R.id.choisir);
        choose.setText("Choose theme :");




        final CircularProgressButton button1 = (CircularProgressButton) findViewById(R.id.buttonList1);
        final CircularProgressButton button2 = (CircularProgressButton) findViewById(R.id.buttonList2);
        final CircularProgressButton button3 = (CircularProgressButton) findViewById(R.id.buttonList3);




        final GiveExerciseActivity self = this;

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetexercise();
            }
        });

        buttonGiveExo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("test", "-> ProActivity tab");
                //String text = dropdown.getSelectedItem().toString();
                Intent intent = new Intent(v.getContext(), InfoPatientActivity.class);
                if ( theme != null &&  type != null  && nameExo != null ) {
                    /*Toast toast = Toast.makeText(getApplicationContext(), "Exo choisi :" + theme + "/" + type + "/" + nameExo + "/", Toast.LENGTH_LONG);
                    toast.show();*/
                    Log.d("TEST", "J'ajoute un exercice");
                    Exercise exercise;
                    exercise = new Exercise();
                    exercise.name = theme;
                    exercise.type = type;
                    exercise.word = nameExo;

                    if (nameExo.equals("Sonometre")){
                        login.addExercise(self, patient_to_add,exercise);
                    } else {

                        login.addExercise(self, patient_to_add, exercise);
                    }
                    startActivity(intent);
                }
                else{
                    Toast toast = Toast.makeText(getApplicationContext(), "Choisir bien l'exercice!", Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });
    }



    public void onClick(final View v) {
        Log.d("TET", "Inside onClick");
        final CircularProgressButton button1 = (CircularProgressButton) findViewById(R.id.buttonList1);
        final CircularProgressButton button2 = (CircularProgressButton) findViewById(R.id.buttonList2);
        final CircularProgressButton button3 = (CircularProgressButton) findViewById(R.id.buttonList3);
        if(button1.getProgress() == 0) {
            switch (v.getId()) {
                case R.id.buttonList1:
                    theme = button1.getIdleText();
                    button1.setCompleteText(typeExo[1].toString());
                    button2.setCompleteText(typeExo[2].toString());
                    button3.setCompleteText("Dommage");
                    break;
                case R.id.buttonList2:
                    theme = button2.getIdleText();
                    button1.setCompleteText("Theme1");
                    button2.setCompleteText("Theme2");
                    button3.setCompleteText("LOOOOOOOL3");
                    break;
                case R.id.buttonList3:
                    theme = button3.getIdleText();
                    button1.setCompleteText("QHQHQHQ1");
                    button2.setCompleteText("AHAHAHA2");
                    button3.setCompleteText("OGOOOHOGOG3");
                    break;

            }
            simulateSuccessProgress(button1);
            simulateSuccessProgress(button2);
            simulateSuccessProgress(button3);
            choose.setText("Choose type :");

        }
        else if (button1.getProgress() == 100) {
            switch(v.getId()) {
                case R.id.buttonList1:
                    type = button1.getCompleteText();
                    break;
                case R.id.buttonList2:
                    type = button2.getCompleteText();
                    break;
                case R.id.buttonList3:
                    type=button3.getCompleteText();
            }
            translateOut = AnimationUtils.makeOutAnimation(getApplicationContext(),false);
            translateOut.setDuration(200);
            button1.startAnimation(translateOut);
            button2.startAnimation(translateOut);
            button3.startAnimation(translateOut);
            translateOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    button1.setVisibility(View.INVISIBLE);
                    button2.setVisibility(View.INVISIBLE);
                    button3.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            choose.setText("Choose exercise :");
            type= type.toLowerCase();
            theme = theme.toLowerCase();
            type = type.replaceAll("\\s+","_");
            theme = theme.replaceAll("\\s+","_");
            Log.d("TEST", theme + " " + type);
            fetchListExercises(theme, type);
        }
    }

    public void fetchListExercises (String theme, String type){
        login.getAllExercices(theme, type, this);
    }

    private void simulateSuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(200);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
            }
        });
        widthAnimation.start();
    }


    public void giveSuccess() {
        Toast toast = Toast.makeText(getApplicationContext(), "Exercice ajouté!", Toast.LENGTH_LONG);
        toast.show();
    }

    public void setExercises(ArrayList<String> exerciseList){
        //this.exerciseList = exerciseList;

        final CustomAllExerciseListAdapter adapter  = new CustomAllExerciseListAdapter(this,
                exerciseList.toArray(new String[exerciseList.size()]));
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.rowlayout, R.id.label, patient_names.toArray(new String[patient_names.size()]));
        setListAdapter(adapter);*/
        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Log.i("test", "Click from the list");
                final String item = (String) parent.getItemAtPosition(position);
                Log.i("test", "Word of exercises is: in this list " + item);
                nameExo = item;
                ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                imageView.setImageResource(R.drawable.ic_done_black_24dp);


            }
        });
        Log.d("test", "j'affiche la liste " + list.getCount());
        translateIn = AnimationUtils.makeInAnimation(getApplicationContext(),false);
        translateIn.setDuration(500);
        list.startAnimation(translateIn);
        list.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if( id == android.R.id.home) {
            System.out.println("Je vais en arrière");
            Intent back = new Intent(getApplicationContext(),previousActivity.getClass());
            Fragment fragment = new TabFragmentInfoPatient2();
            startActivityFromFragment(fragment, back,0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void resetexercise() {
        Log.d("test", "entering  reset");
        theme = null;
        nameExo = null;
        type = null;
        final CircularProgressButton button1 = (CircularProgressButton) findViewById(R.id.buttonList1);
        final CircularProgressButton button2 = (CircularProgressButton) findViewById(R.id.buttonList2);
        final CircularProgressButton button3 = (CircularProgressButton) findViewById(R.id.buttonList3);
        button1.setProgress(0);
        button2.setProgress(0);
        button3.setProgress(0);
        if(!(list == null)) {
            list.setVisibility(View.INVISIBLE);
        }
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
        button3.setVisibility(View.VISIBLE);

    }
}

