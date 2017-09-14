package com.example.user.efluent;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.text.TextUtils;

import com.airbnb.lottie.LottieAnimationView;
import com.dd.CircularProgressButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.widget.TextView;


import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    CircularProgressButton circularButton1;

    LoginManager login;
    private ArrayList<Patient> patient_list;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private TextView mStatusTextView;
    public EditText idField;
    public EditText passwordField;
    public String password;
    public String email;



    // Custom animation speed or duration.
    final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f)
            .setDuration(500);


    final void login(String email, String password) {
        Log.i("test", "Login");
        login.signIn(
                email.toLowerCase(),
                password
        );
    }

    final void login() {
        login.signIn(((EditText) findViewById(R.id.LoginMain)).getText().toString(),
                ((EditText) findViewById(R.id.PasswordMain)).getText().toString()
        );
    }

    public TextView getmStatusTextView() {
        return mStatusTextView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
         mAuth = FirebaseAuth.getInstance();

        login = new LoginManager(this);

        mStatusTextView = (TextView) findViewById(R.id.error);


        mStatusTextView.setVisibility(View.INVISIBLE);

        idField = ((EditText) findViewById(R.id.LoginMain));
        idField.setText("test123@gmail.com");
        passwordField = ((EditText) findViewById(R.id.PasswordMain));
        passwordField.setText("test123");

        circularButton1 = (CircularProgressButton) findViewById(R.id.connexionMain);
        circularButton1.setIndeterminateProgressMode(true);

        circularButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circularButton1.getProgress() == 0) {
                    circularButton1.setProgress(50);
                    password = ((EditText) findViewById(R.id.LoginMain)).getText().toString();
                    email = ((EditText) findViewById(R.id.PasswordMain)).getText().toString();
                    login.signIn(password,email);
                } else if (circularButton1.getProgress() == 100) {
                    circularButton1.setProgress(0);
                }
                else if(circularButton1.getProgress() == -1){
                    circularButton1.setProgress(0);
                }
                else {
                    circularButton1.setProgress(100);
                }

            }
        });

        /*((Button) findViewById(R.id.connexionMain))
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        password = ((EditText) findViewById(R.id.LoginMain)).getText().toString();
                        email = ((EditText) findViewById(R.id.PasswordMain)).getText().toString();
                        loginDialog.setVisibility(View.VISIBLE);
                        login.signIn(password,email);
                    }
                });*/
        final MainActivity self = this;


        final Button buttonInscription = (Button) findViewById(R.id.inscriptionMain);
        buttonInscription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("test", "-> ProActivity tab");
                Intent intent = new Intent(v.getContext(), InscriptionProActivity.class);
                InscriptionProActivity.login = login;
                InscriptionProActivity.oldActivity = self;
                startActivity(intent);


            }
        });

        final Button button = (Button) findViewById(R.id.GoProActivity);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("test", "-> ProActivity tab");
                Intent intent = new Intent(v.getContext(), PatientActivity.class);
                startActivity(intent);


            }
        });
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        /*final Button button2 = (Button) findViewById(R.id.GoPatientActivity);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("test", "-> Patient tab");
                Intent intent = new Intent(v.getContext(), PatientActivity.class);
                //PatientActivity.login = login;
                startActivity(intent);


            }
        });*/



        /*EditText text = new EditText(this);
        //Intent intent1 = new Intent(this, )
        text.setText("Bonjour, c'est bon rentrez chez vous ");
        setContentView(text);*/
    }

    public void showErrorLogin() {
        circularButton1.setProgress(-1);
    }
    public void launchActivity(Intent intent) {
        startActivity(intent);
    }
    public void loginSucessOrtho(Orthophonist ortho) {
        Log.i("test", "Logged was an Ortho");
        Intent intent = new Intent(getApplicationContext(), ProActivity.class);
        ProActivity.login = login;
        ProActivity.ortho = ortho;
        loginSucess(ortho.first_name, intent);
        // the following line is here only for testing
        //login.sendExercise(this, "");
    }

    private void loginSucess(String useremail, final Intent intent) {
        circularButton1.setProgress(100);
        final LottieAnimationView animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        Toast toast = Toast.makeText(getApplicationContext(), "Login successful, Welcome! " + useremail, Toast.LENGTH_LONG);
        toast.show();
        launchActivity(intent);

    }


    public void loginSucessPatient(Patient patient) {
        Log.i("test", "Logged was a patient");
        Intent intent = new Intent(getApplicationContext(), PatientActivity.class);
        PatientActivity.login = login;
        PatientActivity.patient = patient;
        loginSucess(patient.first_name, intent);
    }

    public void giveSuccess() {

    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    public boolean validateForm() {
        boolean valid = true;

        String email = ((EditText) findViewById(R.id.LoginMain)).getText().toString();
        if (TextUtils.isEmpty(email)) {
            ((EditText) findViewById(R.id.LoginMain)).setError("Required.");
            valid = false;
        } else {
            ((EditText) findViewById(R.id.LoginMain)).setError(null);
        }

        String password = ((EditText) findViewById(R.id.PasswordMain)).getText().toString();
        if (TextUtils.isEmpty(password)) {
            ((EditText) findViewById(R.id.PasswordMain)).setError("Required.");
            valid = false;
        } else {
            ((EditText) findViewById(R.id.PasswordMain)).setError(null);
        }

        return valid;
    }

    public void test () {
        Intent resultIntent = new Intent(this, MainActivity.class);
        // Because clicking the notification opens a new ("special") activity, there's
        // no need to create an artificial back stack.
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

    }

}