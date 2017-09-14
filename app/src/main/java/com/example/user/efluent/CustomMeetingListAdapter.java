package com.example.user.efluent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CustomMeetingListAdapter extends ArrayAdapter<Meeting> {
    private final Context context;
    private final Meeting[] values;

    public CustomMeetingListAdapter(Context context, Meeting[] values) {
        super(context, R.layout.rowlayout_meeting, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayout_meeting, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.datetext);
        TextView nameView = (TextView) rowView.findViewById(R.id.nametext);
        android.text.format.DateFormat df = new android.text.format.DateFormat();
        String s = df.format("EEEE dd MMMM yyyy 'at' kk:mm",values[position].date).toString();
        textView.setText(s);
        nameView.setText(values[position].patient.first_name + " " + values[position].patient.last_name);
        // Change the icon for Windows and iPhone
        //Exercise s = values[position];
/*        if (s.startsWith("Windows7") || s.startsWith("iPhone")
                || s.startsWith("Solaris")) {
            imageView.setImageResource(R.drawable.no);
        } else {
            imageView.setImageResource(R.drawable.ok);
        }*/

        return rowView;
    }

}