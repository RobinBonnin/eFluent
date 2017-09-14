package com.example.user.efluent;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import android.widget.Toast;

import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.tasks.Task;

import android.widget.TextView;
import android.content.Context;


public class LoginManager {
    private Orthophonist ortho;
    //private static String ADDRESS = "10.0.2.2:8000/API";
    private static String FULLURL;
    private Context context;

    public  ArrayList<Patient> patient_list;
    public ArrayList<String> id_list;
    public ArrayList<Exercise> exercise_list;
    public ArrayList<String> exerciseList;
    public ArrayList<Message> messages_list;
    public ArrayList<Meeting> meeting_list;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private static final String TAG = "EmailPassword";

    //public void LoginManager
    private MainActivity activity;

    public String token;
    private TextView mStatusTextView;


    public LoginManager(MainActivity activity){
        this.activity = activity;
    }

    public String getUserID() {
        String id;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        id = user.getUid().toString();
        return id;

    }
    public void readData(DatabaseReference ref, final OnGetDataListener listener) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailure();
            }
        });

    }


    public void getOrtho(final String orthoID, DatabaseReference ref, final Patient patient, final AddPatientActivity patientactivity  ) {
        readData(ref, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                ortho = dataSnapshot.getValue(Orthophonist.class);
                Log.d(TAG,orthoID+ "Merci @" + ortho.email);
                addPatient(patient, patientactivity,ortho, orthoID);

            }
            @Override
            public void onStart() {

                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void getPatient(final String patientID, final PatientActivity patientactivity  ) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("Patients").child(patientID);
        readData(ref, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Patient patient;
                patient = dataSnapshot.getValue(Patient.class);
                patientactivity.displayPatient(patient);

            }
            @Override
            public void onStart() {

                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void getOrtho(final String orthoID, final ProActivity proactivity  ) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref = ref.child("Ortho").child(orthoID);
        readData(ref, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Orthophonist ortho;
                ortho = dataSnapshot.getValue(Orthophonist.class);
            }
            @Override
            public void onStart() {

                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure() {

            }
        });
    }


    private String get_item(String response, String key){
        try {
            JSONObject reader = new JSONObject(response);
            //JSONObject tokenOb  = reader.getJSONObject("token");
            return reader.getString(key);

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }


    }

    public void login(String username, String password){
        RequestBody formBody = new FormBody.Builder()
                .add("username", username.toLowerCase())
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(FULLURL + "/api-token-auth/")
                .post(formBody)
                .build();

        Log.i("test", username);
        Log.i("test", password);

        /*client.newCall(request).enqueue(new Callback() {

            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                String out =  response.body().string();
                System.out.println(out);
                if (response.code() == 400){

                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            activity.showErrorLogin();
                        }
                    });
                }
                if (!response.isSuccessful()){
                    throw new IOException("Unexpected code " + response);
                }

                // save token
                token = get_item(out,"token" );
                Log.i("test", "The token is: " + token );

                final String role = get_item(out,"role_name");

                Log.i("test", "role is: " + role);
                if (!(activity == null)){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                                if (role.equals("Patient")) {
                                    activity.loginSucessPatient();
                                } else {
                                    activity.loginSucessOrtho();
                                }

                        }
                    });
                }

            }
        });*/
    }

    public void checkLoginType(final FirebaseUser user) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        final String UserID = user.getUid();
        readData(ref, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotsOrthoID = dataSnapshot.child("Ortho").getChildren().iterator();
                while (dataSnapshotsOrthoID.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsOrthoID.next();
                    Orthophonist ortho = dataSnapshotChild.getValue(Orthophonist.class);// check here whether you are getting the TagName_Chosen
                    if (UserID.equals(ortho.id)) {
                        accountLogged(ortho);
                        return;
                    }
                    Iterator<DataSnapshot> dataSnapshotsPatientID = dataSnapshot.child("Patients").getChildren().iterator();
                    while (dataSnapshotsOrthoID.hasNext()) {
                        DataSnapshot dataSnapshotChild2 = dataSnapshotsPatientID.next();
                        Patient patient = dataSnapshotChild2.getValue(Patient.class);// check here whether you are getting the TagName_Chosen
                        if (UserID.equals(patient.id)) {
                            accountLogged(patient);
                            Log.d(TAG, " I logged a patient");
                            return;
                        }
                    }
                }
            }
            @Override
            public void onStart() {

                Log.d("ONSTART", "Started");
            }

            @Override
            public void onFailure() {



            }
        });
    }

    public void accountLogged(Orthophonist ortho){
            activity.loginSucessOrtho(ortho);
    }
    public void accountLogged(Patient patient){
        activity.loginSucessPatient(patient);
    }

    public void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        email = email.toLowerCase();
        email = email.replaceAll("\\s+","");

        if (!activity.validateForm()) {
            return;
        }
        mAuth = FirebaseAuth.getInstance();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            checkLoginType(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            activity.showErrorLogin();
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            mStatusTextView = activity.getmStatusTextView();
                            mStatusTextView.setText(R.string.app_name);
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    public void reAuthenticate() {
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

// Get auth credentials from the user for re-authentication. The example below shows
// email and password credentials but there are multiple possible providers,
// such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential("user@example.com", "password1234");

// Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "User re-authenticated : " + user.getEmail());
                    }
                });
    }


    public void patientList(final TabFragmentPro2 fragment2) {

        id_list = new ArrayList<String>();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = database.child("Ortho").child(getUserID());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotsPatientID = dataSnapshot.child("Patients").getChildren().iterator();
                while (dataSnapshotsPatientID.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsPatientID.next();
                    String patientID = dataSnapshotChild.getValue(String.class); // check here whether you are getting the TagName_Chosen
                    id_list.add(patientID);
                    Log.d(TAG, "adding a patient to the list");
                    displayPatientList(id_list, ref, fragment2);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
    public void displayPatientList (final ArrayList<String> id_list, DatabaseReference ref,final TabFragmentPro2 fragment2) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        patient_list = new ArrayList<Patient>();
        ref = database.child("Patients");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                patient_list.clear();
                Iterator<DataSnapshot> dataSnapshotsPatientID = dataSnapshot.getChildren().iterator();
                while (dataSnapshotsPatientID.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsPatientID.next();
                    // check here whether you are getting the TagName_Chosen
                    if(id_list.contains(dataSnapshotChild.getKey())) {
                        Patient patient = dataSnapshotChild.getValue(Patient.class);
                        patient_list.add(patient);
                        Log.d(TAG, "Adding one patient " + dataSnapshotChild.getKey() );
                    }
                }
                if (!patient_list.isEmpty()) {
                    Log.d(TAG, " HELLLLLLLLO size list " + patient_list.size() + " " + id_list.size());
                }
                fragment2.setPatient(patient_list);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });


    }


    public void patientList(final TabFragmentPro1 fragment1) {

        id_list = new ArrayList<String>();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = database.child("Ortho").child(getUserID());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotsPatientID = dataSnapshot.child("Patients").getChildren().iterator();
                while (dataSnapshotsPatientID.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsPatientID.next();
                    String patientID = dataSnapshotChild.getValue(String.class); // check here whether you are getting the TagName_Chosen
                    id_list.add(patientID);
                    Log.d(TAG, "adding a patient to the list");
                    displayPatientList(id_list, ref, fragment1);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }
    public void displayPatientList (final ArrayList<String> id_list, DatabaseReference ref,final TabFragmentPro1 fragment1) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();

        patient_list = new ArrayList<Patient>();
        ref = database.child("Patients");
        ref.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              patient_list.clear();
              Iterator<DataSnapshot> dataSnapshotsPatientID = dataSnapshot.getChildren().iterator();
              while (dataSnapshotsPatientID.hasNext()) {
                  DataSnapshot dataSnapshotChild = dataSnapshotsPatientID.next();
                  // check here whether you are getting the TagName_Chosen
                  if(id_list.contains(dataSnapshotChild.getKey())) {
                      Patient patient = dataSnapshotChild.getValue(Patient.class);
                      patient_list.add(patient);
                      Log.d(TAG, "Adding one patient " + dataSnapshotChild.getKey() );
                  }
              }
              if (!patient_list.isEmpty()) {
                  Log.d(TAG, " HELLLLLLLLO size list " + patient_list.size() + " " + id_list.size());
              }
              fragment1.setPatients(patient_list);
          }
          @Override
          public void onCancelled(DatabaseError databaseError) {
              Log.e(TAG, "onCancelled", databaseError.toException());
          }
        });


    }

    /*public void patientList2(final GiveRendezvousActivity activity1){
        Request request = withHeader("/patient_list/").build();

        final LoginManager self = this;

        final ArrayList<Patient> patient_list = new ArrayList<Patient>();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                String out =  response.body().string();
                System.out.println(out);

                try {
                    JSONArray array = new JSONArray(out);

                    for (int i=0; i < array.length(); i++) {
                        Patient patient = new Patient(self);
                        JSONObject patient_json = array.getJSONObject(i);
                        patient.id = patient_json.getString("id");
                        JSONObject user_json = patient_json.getJSONObject("user");
                        patient.first_name = user_json.getString("first_name");
                        patient.last_name = user_json.getString("last_name");
                        patient.email = user_json.getString("email");
                        patient_list.add(patient);
                    }

                    if ( !(null == activity1)) {
                        activity1.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity1.setPatients(patient_list);
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }*/

    public void getAllExercices (String theme, String type, final GiveExerciseActivity giveexercisesactivity){
        exerciseList = new ArrayList<String>();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = database.child("Exercises").child(theme).child(type);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotsExercisesName = dataSnapshot.getChildren().iterator();
                while (dataSnapshotsExercisesName.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsExercisesName.next();
                    String exercise = dataSnapshotChild.getValue(String.class);
                    exercise = exercise.replaceAll("_"," ");// check here whether you are getting the TagName_Chosen
                    exercise = capitalizeFirstLetter(exercise);
                    exerciseList.add(exercise);
                    Log.d(TAG, "adding an exercise to the list " + exercise);
                }
                giveexercisesactivity.setExercises(exerciseList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });
    }

    public void getListOfExercises(Patient patient, final TabFragmentInfoPatient2 infopatient2activity) {
        exercise_list = new ArrayList<Exercise>();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = database.child("Patients").child(patient.id).child("Exercises");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotsExercisesID = dataSnapshot.getChildren().iterator();
                while (dataSnapshotsExercisesID.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsExercisesID.next();
                    Exercise exercise = dataSnapshotChild.getValue(Exercise.class); // check here whether you are getting the TagName_Chosen
                    exercise.type = exercise.type.replaceAll("_", "  ");// check here whether you are getting the TagName_Chosen
                    exercise.type = capitalizeFirstLetter(exercise.type);// check here whether you are getting the TagName_Chosen
                    exercise.word = exercise.word.replaceAll("_", " ");// check here whether you are getting the TagName_Chosen
                    exercise.word = capitalizeFirstLetter(exercise.word);
                    exercise_list.add(exercise);
                    Log.d(TAG, "adding an exercise to the list");
                }
                infopatient2activity.setExercises(exercise_list);
                exercise_list = null;

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

    public void getListOfExercises(Patient patient, final TabFragmentPatient1 infopatient1activity) {
        exercise_list = new ArrayList<Exercise>();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = database.child("Patients").child(patient.id).child("Exercises");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotsExercisesID = dataSnapshot.getChildren().iterator();
                while (dataSnapshotsExercisesID.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsExercisesID.next();
                    Exercise exercise = dataSnapshotChild.getValue(Exercise.class); // check here whether you are getting the TagName_Chosen
                    exercise.type = exercise.type.replaceAll("_", "  ");// check here whether you are getting the TagName_Chosen
                    exercise.type = capitalizeFirstLetter(exercise.type);// check here whether you are getting the TagName_Chosen
                    exercise.word = exercise.word.replaceAll("_", " ");// check here whether you are getting the TagName_Chosen
                    exercise.word = capitalizeFirstLetter(exercise.word);
                    exercise_list.add(exercise);
                    Log.d(TAG, "adding an exercise to the list");
                }
                infopatient1activity.countExercisesNotDone(exercise_list);
                exercise_list = null;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

    public void sendMessage(String text, String title, Patient patient, TabFragmentInfoPatient4 infoPatient4activity){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref = ref.child("Patients").child(patient.id).child("Messages");
        String key =ref.push().getKey();
        Message message = new Message();
        message.id = key;
        message.title = title;
        message.text = text;
        message.time = new Date();
        ref.child(key).setValue(message);
        infoPatient4activity.sentSuccess();
    }

    public void getListOfMessages(final Patient patient, final TabFragmentPatient1 patient1activity) {
        messages_list = new ArrayList<>();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = database.child("Patients").child(patient.id).child("Messages");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotsExercisesID = dataSnapshot.getChildren().iterator();
                while (dataSnapshotsExercisesID.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsExercisesID.next();
                    Message message = dataSnapshotChild.getValue(Message.class); // check here whether you are getting the TagName_Chosen
                    messages_list.add(message);
                    Log.d(TAG, "adding an exercise to the list");
                }
                patient1activity.setMessageList(messages_list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getListOfExercises(Patient patient, final TabFragmentInfoPatient3 infopatient3activity) {
        exercise_list = new ArrayList<Exercise>();
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ref = database.child("Patients").child(patient.id).child("Exercises");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotsExercisesID = dataSnapshot.getChildren().iterator();
                while (dataSnapshotsExercisesID.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsExercisesID.next();
                    Exercise exercise = dataSnapshotChild.getValue(Exercise.class); // check here whether you are getting the TagName_Chosen
                    exercise.type = exercise.type.replaceAll("_","  ");// check here whether you are getting the TagName_Chosen
                    exercise.type =capitalizeFirstLetter(exercise.type);// check here whether you are getting the TagName_Chosen
                    exercise.word=exercise.word.replaceAll("_"," ");// check here whether you are getting the TagName_Chosen
                    exercise.word =capitalizeFirstLetter(exercise.word);
                    exercise_list.add(exercise);
                    Log.d(TAG, "adding an exercise to the list");
                }
                infopatient3activity.setChart(exercise_list);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

    public void getListOfExercises(Patient patient, final TabFragmentPatient2 patient2activity) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, patient.id);
        final DatabaseReference ref = database.child("Patients").child(patient.id).child("Exercises");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotsExercisesID = dataSnapshot.getChildren().iterator();
                exercise_list = new ArrayList<Exercise>();
                while (dataSnapshotsExercisesID.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsExercisesID.next();
                    Exercise exercise = dataSnapshotChild.getValue(Exercise.class);
                    exercise.type=exercise.type.replaceAll("_"," ");// check here whether you are getting the TagName_Chosen
                    exercise.type =capitalizeFirstLetter(exercise.type);// check here whether you are getting the TagName_Chosen
                    exercise.word=exercise.word.replaceAll("_"," ");// check here whether you are getting the TagName_Chosen
                    exercise.word =capitalizeFirstLetter(exercise.word);
                    exercise_list.add(exercise);
                    Log.d(TAG, "adding an exercise to the list");
                }
                patient2activity.setExercises(exercise_list);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

    public void getListOfMeetings(Orthophonist ortho, final TabFragmentPro2 pro2activity) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, ortho.id);
        final DatabaseReference ref = database.child("Ortho").child(ortho.id).child("Meetings");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotsExercisesID = dataSnapshot.getChildren().iterator();
                meeting_list = new ArrayList<Meeting>();
                while (dataSnapshotsExercisesID.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsExercisesID.next();
                    Meeting meeting = dataSnapshotChild.getValue(Meeting.class);
                    meeting_list.add(meeting);
                    Log.d(TAG, "adding a meeting to the list");
                }
                pro2activity.setMeetings(meeting_list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }

    public void getListOfMeetings(Patient patient, final TabFragmentPatient3 patient3activity) {
        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        Log.d(TAG, patient.id);
        final DatabaseReference ref = database.child("Patients").child(patient.id).child("Meetings");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshotsExercisesID = dataSnapshot.getChildren().iterator();
                meeting_list = new ArrayList<Meeting>();
                while (dataSnapshotsExercisesID.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshotsExercisesID.next();
                    Meeting meeting = dataSnapshotChild.getValue(Meeting.class);
                    meeting_list.add(meeting);
                    Log.d(TAG, "adding a meeting to the list");
                }
                Log.d("ETST", "test " + meeting_list.size());
                patient3activity.setMeetings(meeting_list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

    }



    /*            for (int i=0; i < array.length(); i++) {
                        Exercise exercise = new Exercise();
                        JSONObject exercise_json = array.getJSONObject(i);
                        exercise.done = exercise_json.getBoolean("done");
                        exercise.word = exercise_json.getString("word");
                        exercise.id = exercise_json.getInt("id");
                        JSONObject typeJson = exercise_json.getJSONObject("exercise");
                        exercise.type = typeJson.getString("name");
                        //exercise.type = meeting_json.getInt("exercise"); //FIXME it's more than a int
                        exerciseList.add(exercise);
                        //Log.i("LoginManager",exercise.type);

    }

    public void getListOfExercises(final ExerciseReceiver tab, String id) {

        Request request = withHeader("/exercises/"+id+"/").build();
        getListOfExercisesFromRequest(request, tab);

    }

    public void getListOfExercises(final ExerciseReceiver tab) {
        Request request = withHeader("/exercises/").build();
        getListOfExercisesFromRequest(request, tab);
    }*/

    /*public void getListOfMeetings(final MeetingReceiver fragment) {
        Request request = withHeader("/create_meeting/").build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                String out =  response.body().string();
                System.out.println(out);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                final ArrayList<Meeting> meetings_list = new ArrayList<Meeting>();

                try {
                    JSONArray array = new JSONArray(out);
                    for (int i=0; i < array.length(); i++) {
                        Meeting meeting = new Meeting();
                        JSONObject meeting_json = array.getJSONObject(i);
                        meeting.time = format.parse(meeting_json.getString("time"));
                        Log.i("test", "The time of the meeting is: " + meeting_json.getString("time"));
                        meetings_list.add(meeting);
                    }

                    /*A new thread is created for the display of data on the user's side*/
                    //fragment.getActivity().runOnUiThread(new Runnable() {
                    /*if (!(fragment == null)) {
                        fragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                fragment.setMeetings(meetings_list);
                            }
                        });
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        //FIXME this method is too generic
    }
*/
    public void addPatient(final Patient patient, final AddPatientActivity addpatientactivity) {
        // Write a message to the database
        final Orthophonist ortho;
        final String orthoID;
        orthoID = mAuth.getCurrentUser().getUid();

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = database.child("Ortho").child(orthoID);
        getOrtho(orthoID, ref, patient, addpatientactivity);
    }
    public void addPatient(final Patient patient, final AddPatientActivity addpatientactivity,final Orthophonist ortho,final String orthoID) {
        mAuth = FirebaseAuth.getInstance();
        Log.d(TAG, "createAccount:" + patient.email+ortho.username);
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(patient.email, patient.password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference();
                            patient.id = user.getUid();
                            myRef.child("Patients").child(user.getUid()).setValue(patient);
                            myRef.child("Ortho").child(orthoID).child("Patients").child(user.getUid()).setValue(user.getUid());
                            addpatientactivity.singUpSuccess(ortho);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(activity.getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            addpatientactivity.signuperror(task.getException());
                        }

                    }
                });
        // [END create_user_with_email]


    }

    public void exerciseDone(final ExerciseVocal exercisesvocactivity, final Patient patient_to_add, Exercise exercise) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref = ref.child("Patients").child(patient_to_add.id).child("Exercises").child(exercise.id);
        ref.child("done").setValue(true);
        Date d = new Date();
        ref.child("numberOftrials").setValue(exercise.numberOftrials+1);
        ref.child("successDate").setValue(d);
        Log.d("HELLO ", "adding the date of success " + d);

        exercisesvocactivity.goingBack();
    }
    public void exerciseNotDone(final ExerciseVocal exercisesvocactivity, final Patient patient_to_add, Exercise exercise) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref = ref.child("Patients").child(patient_to_add.id).child("Exercises").child(exercise.id).child("numberOftrials");
        ref.setValue(exercise.numberOftrials+1);
        exercisesvocactivity.goingBack();
    }

    /*public void sendExerciseSonometre(final Sonometre act, String ex_id){
        RequestBody formBody = new FormBody.Builder()
                .build();

        Request request = withHeader("/makeexercise/" + ex_id + "/")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                final String reponse_text =  response.body().string();
                Log.i("test",reponse_text);

                if (!(act == null)) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            act.notifyResult(reponse_text);
                        }

                    });
                }

            }
        });

    }*/


   /* public void sendExercise(final ExerciseVocal act, String path, String ex_id){

        Log.i("test","pido ejes");
        // InputStream iS = resources.getAssets().open("bla.txt");
        MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("text/x-markdown; charset=utf-8");

        try {
            File file;
            InputStream inputStream = act.getAssets().open("hello.wav");
            RequestBody requestBody = RequestBodyUtil.create(MEDIA_TYPE_MARKDOWN, inputStream);

            Request request = withHeader("/makeexercise/" + ex_id + "/")
                    .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, new File(path) ))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    final String reponse_text =  response.body().string();
                    Log.i("test",reponse_text);

                    if (!(act == null)) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                    act.notifyResult(reponse_text);
                                }

                        });
                    }

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/

    /** ADD ORTHOPHONISTE TO THE SERVER HERE**/
    public void createOrthophoniste(final Orthophonist ortho, final InscriptionProActivity inscriptionproactivity) {
            mAuth = FirebaseAuth.getInstance();
            Log.d(TAG, "createAccount:" + ortho.email);
            // [START create_user_with_email]
            mAuth.createUserWithEmailAndPassword(ortho.email, ortho.password)
                    .addOnCompleteListener(inscriptionproactivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference();
                                ortho.id = user.getUid();
                                myRef.child("Ortho").child(user.getUid()).setValue(ortho);
                                inscriptionproactivity.singUpSuccess();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(inscriptionproactivity.getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                inscriptionproactivity.signuperror(task.getException());
                            }

                        }
                    });
            // [END create_user_with_email]
        }


    public void addExercise(final GiveExerciseActivity giveexerciseactivity, final Patient patient_to_add, Exercise exercise) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref = ref.child("Patients").child(patient_to_add.id).child("Exercises");
        String key =ref.push().getKey();
        exercise.id = key;
        ref.child(key).setValue(exercise);
        giveexerciseactivity.giveSuccess();
    }

    public void addMeeting(final GiveRendezvousActivity giverendezvousactivity,
                           final Patient patient_to_add, final Orthophonist ortho,
                           Meeting meeting) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref = ref.child("Patients").child(patient_to_add.id).child("Meetings");
        String key=ref.push().getKey();
        meeting.id = key;
        ref.child(key).setValue(meeting);
        ref = database.getReference().child("Ortho").child(ortho.id).child("Meetings");
        ref.child(key).setValue(meeting);
        giverendezvousactivity.giveSuccess();
    }

        /*RequestBody formBody = new FormBody.Builder()
                .add("patient", patient_to_add.id)
                .add("word", nameExo)
                .add("exercise", type.toString())
                .build();

        //Request request = withHeader("/give_exercise/" + type + "/" + nameExo + "/" +patient_to_add.id + "/")
        Request request = withHeader("/give_exercise/")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, final Response response) throws IOException {

                final String text_response = response.body().string();

                if (!(activity == null)) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("test", text_response);
                            activity.giveSuccess();
                        }
                    });
                }
            }
        });

    }*/

    /* public void createRDV(final GiveRendezvousActivity activity, Patient patient, String formatedDate) {
        RequestBody formBody = new FormBody.Builder()
                .add("time", formatedDate)
                .add("patient", patient.id)
                .build();
        Request request = withHeader("/set_RDV/")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, final Response response) throws IOException {

                final String text_response = response.body().string();

                if (!(activity == null)) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("test", text_response);
                            activity.giveSuccess();
                        }
                    });
                }
            }
        });

    }*/

    public void mitrivialFunc(String formatedDate, GiveRendezvousActivity self, Patient choosenPatient) {
        RequestBody formBody = new FormBody.Builder()
                .add("patient", choosenPatient.id)
                .add("time", formatedDate)
                .build();
    }
    public static String capitalizeFirstLetter(String value) {
        if (value == null) {
            return null;
        }
        if (value.length() == 0) {
            return value;
        }
        StringBuilder result = new StringBuilder(value);
        result.replace(0, 1, result.substring(0, 1).toUpperCase());
        return result.toString();
    }




}