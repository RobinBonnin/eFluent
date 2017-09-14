package com.example.user.efluent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;


class GetTime extends TimerTask {

    long start;
    Sonometre sonometre;
    float TIME_SUCCESS = 2;




    public GetTime(Sonometre sonometre){
        restart();
        this.sonometre = sonometre;

    }

    public void run() {

        this.sonometre.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                long current = (System.currentTimeMillis() - start);

                double currentSeconds = ((double) current)/1000;

                // update label
                sonometre.currentTime.setText(String.valueOf(currentSeconds));

                if (currentSeconds > TIME_SUCCESS ){

                    sonometre.exitGood();
                }

                //if ()

            }
        });
    }

    public void restart(){

        start = System.currentTimeMillis();
    }
}

class GetAudio extends TimerTask {
    long start;
    Sonometre sonometre;

    public GetAudio(Sonometre sonometre){
        //start = System.currentTimeMillis();
        this.sonometre = sonometre;
    }


    public void run() {

        sonometre.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sonometre.label.setText(String.valueOf(sonometre.getAmplitude()));
                if (sonometre.getAmplitude() < 1000) {
                    sonometre.comment.setText("Il faut parler plus fort");
                    sonometre.exitWrong();
                }
                else if(sonometre.getAmplitude() > 10000) {
                    sonometre.comment.setText("Il faut parler moins fort");
                    sonometre.exitWrong();
                }
                else {
                    sonometre.comment.setText("Bonne puissance de voix !");

                }
            }
        });
    }
}


public class Sonometre extends AppCompatActivity {

    public static Exercise exercise;
    public static Patient patient;
    public static LoginManager login;
    private int LIIMT = 1000;
    Timer timer;
    Timer timer2;
    public TextView currentTime;
    public TextView label;
    public TextView comment;
    GetTime changeLabelTaskt;
    private ViewGroup zoomGood;
    public static Activity previousActivity;


    @Override
    protected void onCreate (Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_sonometre);

        final Sonometre self = this;
        label = (TextView) findViewById(R.id.sonometre_out);
        comment = (TextView) findViewById(R.id.comment_sonometre);
        currentTime = (TextView) findViewById(R.id.timer);
        final ImageButton button = (ImageButton) findViewById(R.id.micro_sonometre);
        zoomGood= (ViewGroup)findViewById(R.id.grade_good);
        zoomGood.setVisibility(View.INVISIBLE);


        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("test", "-> ProActivity tab");
                start();

                timer = new Timer();
                final int FPS = 5;
                GetAudio audioTaskt = new GetAudio(self);
                timer.scheduleAtFixedRate(audioTaskt, 0, 1000/FPS);

                timer2 = new Timer();
                changeLabelTaskt = new GetTime(self);
                timer2.scheduleAtFixedRate(changeLabelTaskt , 0, 50);

            }
        });
    }

    public void exitWrong(){
        if (!(timer2 == null)) {
            timer2.cancel();
            timer2.purge();
            timer2 = new Timer();
            changeLabelTaskt = new GetTime(this);
            timer2.scheduleAtFixedRate(changeLabelTaskt , 0, 50);
        }
    }

    public void exitGood(){
        if (!(timer2 == null)) {
            timer2.cancel();
            timer2.purge();
            timer2 = null;
        }
        if (!(timer == null)) {
            //login.sendExerciseSonometre(this, exercise.id.toString());
            timer.cancel();
            timer.purge();
            timer = null;
            Animation zoomGood = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
            ViewGroup grade = (ViewGroup)findViewById(R.id.grade_good);
            grade.startAnimation(zoomGood);
            grade.setVisibility(View.VISIBLE);
        }

        // tell the server that it is done

        Log.i("test", "Good!");

    }



    private AudioRecord ar = null;
    private int minSize;

    public void start() {
        minSize= AudioRecord.getMinBufferSize(8000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        ar = new AudioRecord(MediaRecorder.AudioSource.MIC, 8000,AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,minSize);
        ar.startRecording();
    }

    public void stop() {
        if (ar != null) {
            ar.stop();
        }
    }

    public double getAmplitude() {
        short[] buffer = new short[minSize];
        ar.read(buffer, 0, minSize);
        int max = 0;
        for (short s : buffer) {
            if (Math.abs(s) > max) {
                max = Math.abs(s);
            }
        }
        return max;
    }


    public void notifyResult(String reponse_text) {
        //fin
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
            Intent back = new Intent(getApplicationContext(), previousActivity.getClass());
            startActivity(back);
            return true;
        }
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

