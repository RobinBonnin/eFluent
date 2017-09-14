package com.example.user.efluent;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.color.black;

/**
 * Created by Arif on 14/06/2016.
 */
public class SpinAdapter extends ArrayAdapter<Patient> {

    // Your sent context
    private Context context;
    private boolean first_call = true;
    // Your custom values for the spinner (Patient)
    private ArrayList<Patient> patient_list;

    public SpinAdapter(Context context, int textViewResourceId,
                       ArrayList<Patient> patient_list) {
        super(context, textViewResourceId, patient_list);
        this.context = context;
        this.patient_list = patient_list;
    }

    public int getCount(){
        return patient_list.size();
    }

    public Patient getItem(int position){
        return patient_list.get(position);
    }

    /*public long getItemId(int position){
        return position;
    }*/


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        String fullname = patient_list.get(position).first_name;
        String lastname = patient_list.get(position).last_name;
        fullname = fullname.concat(" " + lastname);
        TextView label = new TextView(context);
        label.setTextSize(20);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        if(first_call==true){
            label.setText("Choisissez un patient");
            label.setTextColor(Color.LTGRAY);

        }
        else {
            label.setText(fullname);
            label.setTextColor(Color.BLACK);

        }
        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);

        String fullname = patient_list.get(position).first_name;
        String lastname = patient_list.get(position).last_name;
        fullname = fullname.concat(" " + lastname);
        label.setTextSize(30);
        label.setLineSpacing(2f, 30f);
        label.setTextColor(Color.BLACK);

        label.setText(fullname);

        return label;
    }
    public boolean first_call(){
        first_call = false;
        return first_call;
    }
}
