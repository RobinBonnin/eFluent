package com.example.user.efluent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CustomExerciseListAdapter extends ArrayAdapter<Exercise> {
    private final Context context;
    private final Exercise[] values;

    public CustomExerciseListAdapter(Context context, Exercise[] values) {
        super(context, R.layout.rowlayout2, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout2, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.label);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(values[position].type + " : " + values[position].word);
        // Change the icon for Windows and iPhone
        //Exercise s = values[position];
/*        if (s.startsWith("Windows7") || s.startsWith("iPhone")
                || s.startsWith("Solaris")) {
            imageView.setImageResource(R.drawable.no);
        } else {
            imageView.setImageResource(R.drawable.ok);
        }*/

        if (values[position].type.equals("Sonometre")){
            textView.setText("Sonometre");
        }

        if (values[position].done){
            imageView.setImageResource(R.drawable.ic_done_black_24dp);
        } else {
            imageView.setImageResource(R.drawable.ic_today_black_48dp);
        }
        return rowView;
    }

}